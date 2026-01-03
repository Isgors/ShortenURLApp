package dev.igordesouza.shortenurlapp.presentation.di

import dev.igordesouza.orthos.runtime.OrthosRuntime
import dev.igordesouza.shortenurlapp.presentation.home.HomeViewModel
import dev.igordesouza.shortenurlapp.presentation.security.OrthosVerdictViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    single {
        OrthosRuntime.Builder(androidContext()).build()
    }
    viewModel {
        HomeViewModel(
            shortenUrlUseCase = get(),
            observeUrlsUseCase = get(),
            deleteUrlUseCase = get(),
            deleteAllUrlsUseCase = get()
        )
    }

    viewModel {
        OrthosVerdictViewModel(
            get<OrthosRuntime>()
        )
    }

}