package dev.igordesouza.orthos.runtime.feature.repository

import dev.igordesouza.orthos.runtime.feature.FeatureSnapshot

import dev.igordesouza.orthos.core.identity.ClientIdentity
import dev.igordesouza.orthos.runtime.feature.datasource.LocalFeatureDataSource
import dev.igordesouza.orthos.runtime.feature.datasource.RemoteFeatureDataSource

class FeatureRepositoryImpl(
    private val local: LocalFeatureDataSource,
    private val remote: RemoteFeatureDataSource
) : FeatureRepository {

    override fun getSnapshot(identity: ClientIdentity): FeatureSnapshot {
        val cached = local.get()
        if (cached != null) return cached

        val fetched = remote.fetch(identity)
        if (fetched != null) {
            local.save(fetched)
            return fetched
        }

        error("No feature snapshot available")
    }
}
