package dev.igordesouza.orthos.core.feature

import dev.igordesouza.orthos.core.signal.SignalType
import kotlinx.serialization.Serializable

/**
 * Group-level feature configuration.
 */
@Serializable
data class SignalGroupConfig(
    val type: SignalType,
    val enabled: Boolean,
    val defaultWeight: Int
)
