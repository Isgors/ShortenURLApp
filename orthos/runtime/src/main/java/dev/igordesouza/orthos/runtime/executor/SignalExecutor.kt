package dev.igordesouza.orthos.runtime.executor

import dev.igordesouza.orthos.runtime.signal.Signal
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext

/**
 * Executes a collection of signals and returns
 * their collected results.
 */
interface SignalExecutor {

    fun execute(
        signals: List<Signal>,
        context: RuntimeSignalContext
    ): List<SignalResult>
}

