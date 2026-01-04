package dev.igordesouza.shortenurlapp

import android.app.Application
import dev.igordesouza.shortenurlapp.data.di.dataModule
import dev.igordesouza.shortenurlapp.domain.di.domainModule
import dev.igordesouza.shortenurlapp.presentation.di.presentationModule
import dev.igordesouza.shortenurlapp.util.ReleaseTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(presentationModule, dataModule, domainModule)
        }
    }
}