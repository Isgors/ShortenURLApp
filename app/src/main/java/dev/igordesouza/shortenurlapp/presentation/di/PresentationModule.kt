package dev.igordesouza.shortenurlapp.presentation.di

import dev.igordesouza.shortenurlapp.presentation.home.HomeViewModelImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        HomeViewModelImpl(
            shortenUrlUseCase = get(),
            getRecentlyShortenedUrlsUseCase = get(),
            deleteUrlUseCase = get(),
            deleteAllUrlsUseCase = get()
        )
    }
}