package dev.igordesouza.shortenurlapp.presentation.di

import dev.igordesouza.shortenurlapp.presentation.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        HomeViewModel(
            shortenUrlUseCase = get(),
            observeUrlsUseCase = get(),
            deleteUrlUseCase = get(),
            deleteAllUrlsUseCase = get()
        )
    }
}