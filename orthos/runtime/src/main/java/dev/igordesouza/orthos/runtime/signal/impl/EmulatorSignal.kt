package dev.igordesouza.orthos.runtime.signal.impl

import android.os.Build
import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.core.signal.SignalType
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext

/**
 * Detects execution inside a standard Android emulator.
 *
 * This signal relies on well-known emulator fingerprints
 * present in Build properties.
 */
class EmulatorSignal : Signal {

    override val id: SignalId = SignalId.EMULATOR
    override val type = SignalType.ENVIRONMENT

    override fun collect(context: RuntimeSignalContext): SignalResult {
        val triggered = isProbablyEmulator()

        return SignalResult(
            signalId = id,
            signalType = type,
            triggered = triggered,
            confidence = if (triggered) 0.85 else 0.0,
            metadata = if (triggered) {
                mapOf(
                    "fingerprint" to Build.FINGERPRINT,
                    "model" to Build.MODEL
                )
            } else {
                emptyMap()
            }
        )
    }

    private fun isProbablyEmulator(): Boolean {
        return (
                Build.FINGERPRINT.startsWith("generic") ||
                        Build.FINGERPRINT.contains("emulator") ||
                        Build.MODEL.contains("google_sdk") ||
                        Build.MODEL.contains("Emulator") ||
                        Build.MODEL.contains("Android SDK built for x86") ||
                        Build.MANUFACTURER.contains("Genymotion") ||
                        Build.BRAND.startsWith("generic") ||
                        Build.DEVICE.startsWith("generic") ||
                        Build.PRODUCT.contains("sdk")
                )
    }
}
