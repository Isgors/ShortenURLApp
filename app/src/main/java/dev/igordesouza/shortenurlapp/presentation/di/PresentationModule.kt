package dev.igordesouza.shortenurlapp.presentation.di

import dev.igordesouza.orthos.sdk.OrthosConfig
import dev.igordesouza.orthos.sdk.OrthosRuntimeApi
import dev.igordesouza.orthos.runtime.policy.failsafe.ConservativeFailSafeHandler
import dev.igordesouza.orthos.runtime.policy.failsafe.PermissiveFailSafeHandler
import dev.igordesouza.orthos.sdk.Orthos
import dev.igordesouza.shortenurlapp.BuildConfig
import dev.igordesouza.shortenurlapp.presentation.home.HomeViewModel
import dev.igordesouza.shortenurlapp.presentation.security.OrthosGateViewModel
import dev.igordesouza.shortenurlapp.presentation.security.OrthosVerdictViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val presentationModule = module {

    single {
        Orthos.install(
            context = androidContext(),
            enabledFromConsumer = BuildConfig.ORTHOS_ENABLED,
            config = OrthosConfig(
                failSafeHandler = if (BuildConfig.DEBUG)
                    PermissiveFailSafeHandler()
                else
                    ConservativeFailSafeHandler()
            )
        )
    }
    
    // 2) Expose runtime API to ViewModels / Gate
    single<OrthosRuntimeApi> {
        get<Orthos>().runtime()
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
            orthosRuntime = get<OrthosRuntimeApi>()
        )
    }

    viewModel {
        OrthosGateViewModel(
            orthos = get()
        )
    }
}