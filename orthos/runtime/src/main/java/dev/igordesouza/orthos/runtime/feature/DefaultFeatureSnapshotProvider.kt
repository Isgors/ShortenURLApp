package dev.igordesouza.orthos.runtime.feature

import dev.igordesouza.orthos.core.feature.FeatureSnapshot
import dev.igordesouza.orthos.core.identity.ClientIdentity
import dev.igordesouza.orthos.runtime.feature.repository.FeatureRepository

/**
 * Default implementation backed by FeatureRepository.
 */
class DefaultFeatureSnapshotProvider(
    private val repository: FeatureRepository
) : FeatureSnapshotProvider {

    override fun get(identity: ClientIdentity): FeatureSnapshot {
        return repository.getSnapshot(identity)
    }
}
