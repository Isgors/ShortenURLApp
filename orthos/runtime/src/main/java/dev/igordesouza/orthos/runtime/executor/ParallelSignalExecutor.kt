package dev.igordesouza.orthos.runtime.executor

import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.core.signal.SignalType
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext
import dev.igordesouza.orthos.runtime.signal.Signal
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Executes signals in parallel using a fixed thread pool.
 */
class ParallelSignalExecutor(
    private val threadCount: Int = Runtime.getRuntime().availableProcessors()
) : SignalExecutor {

    private val executor = Executors.newFixedThreadPool(threadCount)

    override fun execute(
        signals: List<Signal>,
        context: RuntimeSignalContext
    ): List<SignalResult> {
        val futures: List<Future<SignalResult>> =
            signals.map { signal ->
                executor.submit<SignalResult> {
                    signal.collect(context)
                }
            }

        return futures.map { future ->
            try {
                future.get()
            } catch (t: Throwable) {
                SignalResult(
                    signalId = SignalId.UNKNOWN,
                    signalType = SignalType.RUNTIME,
                    triggered = true,
                    confidence = 0.5f,
                    metadata = mapOf("executorError" to t.message.orEmpty())
                )
            }
        }
    }
}
