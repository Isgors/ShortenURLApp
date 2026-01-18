package dev.igordesouza.orthos.sdk

import android.content.Context

/**
 * Single point of truth for whether Orthos is enabled.
 *
 * Priority:
 * 1) DevTools override (if devtools module is present)
 * 2) BuildConfig / consumer-provided flag (enabledFromConsumer)
 */
object OrthosVariantGate {

    fun isEnabled(context: Context, enabledFromConsumer: Boolean): Boolean {
        val appContext = context.applicationContext

        return when (readDevToolsOverride(appContext)) {
            DevOverride.FORCE_ON -> true
            DevOverride.FORCE_OFF -> false
            DevOverride.DEFAULT -> enabledFromConsumer
        }
    }

    private enum class DevOverride { DEFAULT, FORCE_ON, FORCE_OFF }

    /**
     * Reads override from orthos-devtools WITHOUT compile-time dependency.
     */
    private fun readDevToolsOverride(context: Context): DevOverride {
        return try {
            val clazz = Class.forName("dev.igordesouza.orthos.devtools.OrthosDevTools")
            val method = clazz.getMethod("getForceModeBlocking", Context::class.java)
            val mode = method.invoke(null, context) as Int

            when (mode) {
                1 -> DevOverride.FORCE_ON
                2 -> DevOverride.FORCE_OFF
                else -> DevOverride.DEFAULT
            }
        } catch (_: Throwable) {
            DevOverride.DEFAULT
        }
    }
}