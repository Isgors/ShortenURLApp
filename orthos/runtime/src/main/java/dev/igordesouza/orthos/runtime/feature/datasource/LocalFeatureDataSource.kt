package dev.igordesouza.orthos.runtime.feature.datasource

import dev.igordesouza.orthos.core.feature.FeatureSnapshot

/**
 * Local offline-first storage for feature configuration.
 */
interface LocalFeatureDataSource {

    fun get(): FeatureSnapshot?
    fun save(snapshot: FeatureSnapshot)
}
