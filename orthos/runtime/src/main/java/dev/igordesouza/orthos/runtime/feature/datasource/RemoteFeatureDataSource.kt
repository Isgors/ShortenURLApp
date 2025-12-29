package dev.igordesouza.orthos.runtime.feature.datasource

import dev.igordesouza.orthos.core.feature.FeatureSnapshot
import dev.igordesouza.orthos.core.identity.ClientIdentity

/**
 * Fetches feature configuration from a remote source.
 */
interface RemoteFeatureDataSource {

    fun fetch(identity: ClientIdentity): FeatureSnapshot?
}
