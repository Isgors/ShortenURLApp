package dev.igordesouza.orthos.sdk

import android.content.Context
import java.lang.ref.WeakReference

/**
 * Firebase-like entry point.
 *
 * Usage:
 *   val orthos = Orthos.install(
 *      context = context,
 *      enabled = BuildConfig.ORTHOS_ENABLED
 *   )
 *   val verdict = orthos.runtime().evaluate()
 */
class Orthos private constructor(
    private val context: Context,
    private val config: OrthosConfig,
    private val enabled: Boolean
) {

    private val runtimeApi: OrthosRuntimeApi by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        if (!enabled) {
            NoOpOrthosRuntime()
        } else {
            RealOrthosRuntime(
                context = context,
                failSafeHandler = config.failSafeHandler
            )
        }
    }

    fun isEnabled(): Boolean = enabled

    fun runtime(): OrthosRuntimeApi = runtimeApi

    companion object {
        @Volatile
        private var cachedRef: WeakReference<Orthos>? = null

        /**
         * Installs Orthos using gate (variant flag + devtools override).
         * Caches instance to behave like Firebase.
         */
        @JvmStatic
        fun install(
            context: Context,
            enabledFromConsumer: Boolean,
            config: OrthosConfig = OrthosConfig()
        ): Orthos {
            cachedRef?.get()?.let { return it }

            val appContext = context.applicationContext
            val instance = Orthos(
                context = appContext,
                config = config,
                enabled = OrthosVariantGate.isEnabled(appContext, enabledFromConsumer)
            )
            cachedRef = WeakReference(instance)
            return instance
        }

        /**
         * Clears cached instance so next install() re-reads gate/override.
         * Devtools uses this after applying overrides.
         */
        @JvmStatic
        fun reset() {
            cachedRef = null
        }
    }
}
