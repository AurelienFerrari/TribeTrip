package com.example.tribetrip.backend.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI
import javax.sql.DataSource

/**
 * Neon (and most managed Postgres providers) hand out a single connection string in the
 * `postgresql://user:password@host/db?sslmode=require` form, which the plain JDBC driver
 * cannot parse directly. This turns the raw DATABASE_URL env var into a HikariDataSource
 * without ever needing separate username/password variables.
 */
@Configuration
class DataSourceConfig {

    @Bean
    fun dataSource(): DataSource {
        val databaseUrl = System.getenv("DATABASE_URL")
            ?: error("DATABASE_URL environment variable is not set")

        val uri = URI(databaseUrl)
        val (username, password) = uri.userInfo.split(":", limit = 2)
        val port = if (uri.port != -1) ":${uri.port}" else ""

        // Ensure sslmode is always set, whatever the raw URL does or doesn't already specify —
        // never allow a silently unencrypted connection to Neon.
        val queryParams = (uri.query ?: "")
            .split("&")
            .filter { it.isNotBlank() }
            .associate { param ->
                val parts = param.split("=", limit = 2)
                parts[0] to (parts.getOrNull(1) ?: "")
            }
            .toMutableMap()
        queryParams.putIfAbsent("sslmode", "require")
        val query = queryParams.entries.joinToString("&") { "${it.key}=${it.value}" }

        val jdbcUrl = "jdbc:postgresql://${uri.host}$port${uri.path}?$query"

        return HikariDataSource().apply {
            this.jdbcUrl = jdbcUrl
            this.username = username
            this.password = password
            driverClassName = "org.postgresql.Driver"
        }
    }
}
