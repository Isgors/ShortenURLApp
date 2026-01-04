package dev.igordesouza.orthos.runtime.feature

import dev.igordesouza.orthos.core.feature.SignalFeatureConfig
import dev.igordesouza.orthos.core.feature.SignalGroupConfig
import dev.igordesouza.orthos.core.identity.ClientIdentity
import dev.igordesouza.orthos.core.signal.SignalId
import dev.igordesouza.orthos.core.signal.SignalType
import dev.igordesouza.orthos.runtime.policy.Policy
import kotlinx.serialization.Serializable

/**
 * Immutable snapshot used at runtime.
 *
 * Once created, behavior must not change.
 */
@Serializable
data class FeatureSnapshot(
    val identity: ClientIdentity,
    val signals: Map<SignalId, SignalFeatureConfig>,
    val groups: Map<SignalType, SignalGroupConfig>,
    val policy: Policy
)