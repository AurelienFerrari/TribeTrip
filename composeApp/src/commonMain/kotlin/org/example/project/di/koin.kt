package org.example.project.di

import org.example.project.viewmodel.HomeViewModel
import org.example.project.viewmodel.ProfileViewModel
import org.example.project.viewmodel.SearchViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun initKoin() {
    startKoin {
        modules(viewModelModule)
    }
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::ProfileViewModel)
}