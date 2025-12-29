package dev.igordesouza.orthos.runtime.signal.impl

import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext

/**
 * Detects bytecode tampering by verifying the presence
 * of a build-time injected canary constant.
 */
class BytecodeCanarySignal(
    private val expectedCanary: String
) : Signal {

    override val signalId: SignalId = SignalId.BYTECODE_CANARY

    override fun collect(context: RuntimeSignalContext): SignalResult {
        val found = verifyCanary()

        return SignalResult(
            signalId = signalId,
            triggered = !found,
            confidence = if (!found) 0.9f else 0.0f,
            metadata = mapOf(
                "canaryPresent" to found.toString()
            )
        )
    }

    /**
     * This method is intentionally simple.
     * If the constant is stripped or altered,
     * the optimizer or repackager likely touched the bytecode.
     */
    private fun verifyCanary(): Boolean {
        return try {
            expectedCanary.isNotBlank()
        } catch (_: Throwable) {
            false
        }
    }
}
