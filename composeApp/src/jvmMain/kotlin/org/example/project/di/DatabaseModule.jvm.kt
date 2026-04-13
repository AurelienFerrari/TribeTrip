package org.example.project.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.example.project.db.SeenItDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseModule(): Module = module {
    single {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
            SeenItDatabase.Schema.create(it)
        }
        SeenItDatabase(driver)
    }
}