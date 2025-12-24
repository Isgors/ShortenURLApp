package dev.igordesouza.shortenurlapp.domain.di

import dev.igordesouza.shortenurlapp.domain.usecase.DeleteAllUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteAllUrlsUseCaseImpl
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteUrlUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteUrlUseCaseImpl
import dev.igordesouza.shortenurlapp.domain.usecase.ObserveUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.ObserveUrlsUseCaseImpl
import dev.igordesouza.shortenurlapp.domain.usecase.ShortenUrlUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.ShortenUrlUseCaseImpl
import org.koin.dsl.module

val domainModule = module {
    factory<ShortenUrlUseCase> { ShortenUrlUseCaseImpl(get()) }
    factory<ObserveUrlsUseCase> { ObserveUrlsUseCaseImpl(get()) }
    factory<DeleteUrlUseCase> { DeleteUrlUseCaseImpl(get()) }
    factory<DeleteAllUrlsUseCase> { DeleteAllUrlsUseCaseImpl(get()) }
}
