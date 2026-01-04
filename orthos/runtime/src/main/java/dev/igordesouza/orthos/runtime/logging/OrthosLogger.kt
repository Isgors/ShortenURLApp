package dev.igordesouza.orthos.runtime.logging

import timber.log.Timber

internal object OrthosLogger {

    fun trace(message: String, vararg args: Any?) {
        Timber.tag("Orthos").v(message, *args)
    }

    fun debug(message: String, vararg args: Any?) {
        Timber.tag("Orthos").d(message, *args)
    }

    fun info(message: String, vararg args: Any?) {
        Timber.tag("Orthos").i(message, *args)
    }

    fun warn(message: String, vararg args: Any?) {
        Timber.tag("Orthos").w(message, *args)
    }

    fun error(
        throwable: Throwable? = null,
        message: String,
        vararg args: Any?
    ) {
        Timber.tag("Orthos").e(throwable, message, *args)
    }
}
