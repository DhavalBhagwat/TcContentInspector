package com.tc.example.di

import com.tc.example.ui.activities.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for app layer dependencies.
 */
val appModule = module {
    viewModel { MainViewModel(get(), get(), get(), get()) }
}