package dev.igordesouza.shortenurlapp.domain.di

import dev.igordesouza.shortenurlapp.domain.usecase.DeleteAllUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteUrlUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.GetRecentlyShortenedUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.ShortenUrlUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { ShortenUrlUseCase(get()) }
    factory { GetRecentlyShortenedUrlsUseCase(get()) }
    factory { DeleteUrlUseCase(get()) }
    factory { DeleteAllUrlsUseCase(get()) }
}
