package dev.igordesouza.orthos.core.signal

/**
 * Static description of a Signal.
 *
 * Lives in core so it can be:
 * - Referenced by plugin
 * - Used by feature flags
 * - Used by policies
 */
data class SignalDescriptor(
    val id: SignalId,
    val type: SignalType,
    val defaultWeight: Int
)
