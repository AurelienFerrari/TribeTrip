package org.example.project.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.example.project.db.SeenItDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun databaseModule(): Module = module {
    single {
        val driver = NativeSqliteDriver(SeenItDatabase.Schema, "seenit.db")
        SeenItDatabase(driver)
    }
}