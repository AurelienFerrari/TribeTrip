package org.example.project.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.example.project.db.SeenItDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseModule(): Module = module {
    single {
        val driver = AndroidSqliteDriver(SeenItDatabase.Schema, get(), "seenit.db")
        SeenItDatabase(driver)
    }
}