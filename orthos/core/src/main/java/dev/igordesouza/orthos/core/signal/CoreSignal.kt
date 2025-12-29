package dev.igordesouza.orthos.core.signal

/**
 * Contract for all runtime Signals.
 *
 * Signals must:
 * - Be deterministic
 * - Avoid side effects
 * - Return quickly
 */
interface CoreSignal {

    /**
     * Descriptor describing this signal.
     */
    val descriptor: SignalDescriptor

    /**
     * Executes the detection logic.
     */
    fun detect(): SignalResult
}
