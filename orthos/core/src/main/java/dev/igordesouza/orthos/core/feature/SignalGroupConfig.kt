package dev.igordesouza.orthos.core.feature

import dev.igordesouza.orthos.core.signal.SignalType

/**
 * Group-level feature configuration.
 */
data class SignalGroupConfig(
    val type: SignalType,
    val enabled: Boolean,
    val defaultWeight: Int
)
