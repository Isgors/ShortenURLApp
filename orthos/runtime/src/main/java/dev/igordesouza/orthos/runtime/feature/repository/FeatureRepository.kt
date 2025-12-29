package dev.igordesouza.orthos.runtime.feature.repository

import dev.igordesouza.orthos.core.feature.FeatureSnapshot
import dev.igordesouza.orthos.core.identity.ClientIdentity

/**
 * Repository abstraction for feature configuration.
 */
interface FeatureRepository {
    fun getSnapshot(identity: ClientIdentity): FeatureSnapshot
}
