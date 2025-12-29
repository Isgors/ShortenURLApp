package dev.igordesouza.orthos.core.feature

import dev.igordesouza.orthos.core.signal.SignalId

/**
 * Feature configuration for a single signal.
 */
data class SignalFeatureConfig(
    val signalId: SignalId,
    val enabled: Boolean,
    val weight: Int
)
