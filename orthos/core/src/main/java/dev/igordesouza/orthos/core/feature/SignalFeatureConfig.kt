package dev.igordesouza.orthos.core.feature

import dev.igordesouza.orthos.core.signal.SignalId
import kotlinx.serialization.Serializable

/**
 * Feature configuration for a single signal.
 */
@Serializable
data class SignalFeatureConfig(
    val signalId: SignalId,
    val enabled: Boolean,
    val weight: Int
)
