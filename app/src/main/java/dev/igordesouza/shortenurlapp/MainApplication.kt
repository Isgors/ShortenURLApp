package dev.igordesouza.shortenurlapp

import android.app.Application
import dev.igordesouza.shortenurlapp.data.di.dataModule
import dev.igordesouza.shortenurlapp.domain.di.domainModule
import dev.igordesouza.shortenurlapp.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(presentationModule, dataModule, domainModule)
        }
    }
}