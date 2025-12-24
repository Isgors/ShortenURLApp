package dev.igordesouza.shortenurlapp.data.di

import androidx.room.Room
import dev.igordesouza.shortenurlapp.data.local.database.UrlDao
import dev.igordesouza.shortenurlapp.data.local.datasource.UrlShortenerLocalDataSource
import dev.igordesouza.shortenurlapp.data.local.datasource.UrlShortenerLocalDataSourceImpl
import dev.igordesouza.shortenurlapp.data.local.database.UrlShortenerDatabase
import dev.igordesouza.shortenurlapp.data.remote.datasource.UrlShortenerRemoteDataSource
import dev.igordesouza.shortenurlapp.data.remote.datasource.UrlShortenerRemoteDataSourceImpl
import dev.igordesouza.shortenurlapp.data.remote.network.UrlShortenerService
import dev.igordesouza.shortenurlapp.data.repository.UrlRepositoryImpl
import dev.igordesouza.shortenurlapp.domain.repository.UrlRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
    single {
        Room.databaseBuilder(
            androidApplication(),
            UrlShortenerDatabase::class.java,
            "url-shortener-db"
        ).build()
    }
    single<UrlDao> { get<UrlShortenerDatabase>().urlDao() }
    single<UrlShortenerLocalDataSource> { UrlShortenerLocalDataSourceImpl(get()) }
    single<UrlShortenerRemoteDataSource> { UrlShortenerRemoteDataSourceImpl(get()) }
    single { UrlShortenerService(get()) }
    single<UrlRepository> { UrlRepositoryImpl(get(), get()) }
}
