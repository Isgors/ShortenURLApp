package dev.igordesouza.orthos.core.feature

import dev.igordesouza.orthos.core.identity.ClientIdentity
import dev.igordesouza.orthos.core.policy.DetectionPolicy
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalType

/**
 * Immutable snapshot used at runtime.
 *
 * Once created, behavior must not change.
 */
data class FeatureSnapshot(
    val identity: ClientIdentity,
    val signals: Map<SignalId, SignalFeatureConfig>,
    val groups: Map<SignalType, SignalGroupConfig>,
    val policy: DetectionPolicy
)
