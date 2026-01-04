package dev.igordesouza.orthos.runtime.signal.impl

import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.core.signal.SignalType
import java.io.File
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext

/**
 * Detects whether the device is rooted.
 *
 * This signal performs multiple lightweight checks:
 * - Presence of known root binaries
 * - Existence of dangerous system paths
 *
 * This is a heuristic-based signal.
 */
class RootSignal : Signal {

    override val id: SignalId = SignalId.ROOT
    override val type: SignalType = SignalType.RUNTIME

    private val knownRootPaths = listOf(
        "/system/bin/su",
        "/system/xbin/su",
        "/sbin/su",
        "/system/app/Superuser.apk",
        "/system/app/Magisk.apk",
        "/data/local/xbin/su"
    )

    override fun collect(context: RuntimeSignalContext): SignalResult {
        val triggered = knownRootPaths.any { path ->
            File(path).exists()
        }

        return SignalResult(
            signalId = id,
            signalType = type,
            triggered = triggered,
            confidence = if (triggered) 0.9 else 0.0,
            metadata = if (triggered) {
                mapOf("reason" to "known root path detected")
            } else {
                emptyMap()
            }
        )
    }
}
