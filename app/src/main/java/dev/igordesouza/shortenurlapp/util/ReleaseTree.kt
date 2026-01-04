package dev.igordesouza.shortenurlapp.util

import android.util.Log
import timber.log.Timber

class ReleaseTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        // só warnings e erros
        return priority >= Log.WARN
    }

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        // opcional: enviar pra Crashlytics / backend
    }
}