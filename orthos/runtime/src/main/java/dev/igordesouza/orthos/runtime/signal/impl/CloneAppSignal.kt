package dev.igordesouza.orthos.runtime.signal.impl

import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext
import java.io.File

/**
 * Detects cloned application instances.
 *
 * This signal identifies:
 * - Abnormal application data directories
 * - Multiple execution sandboxes
 *
 * Requires a stable RuntimeIdentity to be effective.
 */
class CloneAppSignal : Signal {

    override val signalId: SignalId = SignalId.CLONE_APP

    override fun collect(context: RuntimeSignalContext): SignalResult {
        val appDataDir = context.applicationContext.applicationInfo.dataDir
        val triggered = isClonedEnvironment(appDataDir)

        return SignalResult(
            signalId = signalId,
            triggered = triggered,
            confidence = if (triggered) 0.8f else 0.0f,
            metadata = if (triggered) {
                mapOf(
                    "dataDir" to appDataDir
                )
            } else {
                emptyMap()
            }
        )
    }

    private fun isClonedEnvironment(dataDir: String): Boolean {
        // Common cloning patterns
        return listOf(
            "/data/user/999",
            "/data/data/com.parallel",
            "/data/data/com.lbe.parallel"
        ).any { suspicious ->
            dataDir.contains(suspicious) || File(suspicious).exists()
        }
    }
}
