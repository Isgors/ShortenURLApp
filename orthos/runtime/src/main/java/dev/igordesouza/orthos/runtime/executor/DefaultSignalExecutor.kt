package dev.igordesouza.orthos.runtime.executor

import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext
import dev.igordesouza.orthos.runtime.signal.Signal

/**
 * Default sequential implementation of SignalExecutor.
 */
class DefaultSignalExecutor : SignalExecutor {

    override fun execute(
        signals: List<Signal>,
        context: RuntimeSignalContext
    ): List<SignalResult> {
        return signals.map { signal ->
            try {
                signal.collect(context)
            } catch (t: Throwable) {
                SignalResult(
                    signalId = signal.id,
                    triggered = true,
                    confidence = 0.5f,
                    metadata = mapOf(
                        "error" to (t.message ?: "unknown")
                    )
                )
            }
        }
    }
}
