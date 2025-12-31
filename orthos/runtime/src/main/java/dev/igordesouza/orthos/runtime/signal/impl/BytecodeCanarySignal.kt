package dev.igordesouza.orthos.runtime.signal.impl

import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.core.signal.SignalType
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext
import dev.igordesouza.orthos.runtime.signal.Signal

/**
 * Verifies bytecode integrity by validating a canary
 * injected at build time.
 */
class BytecodeCanarySignal : Signal {

    override val id = SignalId.BYTECODE_CANARY
    override val type = SignalType.TAMPERING

    override fun collect(context: RuntimeSignalContext): SignalResult {
        return try {
            val method = context.applicationContext
                .javaClass
                .getDeclaredMethod("__orthos_canary_value")

            method.isAccessible = true
            val value = method.invoke(null) as Long

            SignalResult(
                signalId = id,
                signalType = type,
                triggered = value == 0L,
                confidence = 1.0f
            )
        } catch (t: Throwable) {
            SignalResult(
                signalId = id,
                signalType = type,
                triggered = true,
                confidence = 1.0f
            )
        }
    }
}
