package dev.igordesouza.orthos.runtime.feature

import dev.igordesouza.orthos.core.feature.FeatureSnapshot
import dev.igordesouza.orthos.core.identity.ClientIdentity

/**
 * Provides the feature configuration snapshot
 * used during runtime evaluation.
 */
interface FeatureSnapshotProvider {

    fun get(identity: ClientIdentity): FeatureSnapshot
}
