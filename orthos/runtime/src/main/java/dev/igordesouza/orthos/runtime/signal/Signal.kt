package dev.igordesouza.orthos.runtime.signal

import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.runtime.context.RuntimeSignalContext

/**
 * Represents a concrete runtime security signal.
 *
 * A Signal executes a deterministic security check against
 * the current execution environment.
 *
 * Implementations must:
 * - Be side-effect free
 * - Be fast (no I/O or blocking calls)
 * - Be resilient to basic hooking attempts
 */
interface Signal {

    /**
     * Unique identifier linking this signal to core configuration.
     */
    val signalId: SignalId

    /**
     * Executes the signal detection logic.
     *
     * @param context Runtime context with system and app information.
     * @return SignalResult containing detection outcome.
     */
    fun collect(context: RuntimeSignalContext): SignalResult
}
