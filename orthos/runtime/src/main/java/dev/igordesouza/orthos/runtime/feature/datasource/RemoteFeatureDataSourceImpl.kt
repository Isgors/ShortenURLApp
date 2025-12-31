package dev.igordesouza.orthos.runtime.feature.datasource

import dev.igordesouza.orthos.core.feature.FeatureSnapshot
import dev.igordesouza.orthos.core.identity.ClientIdentity
import kotlinx.serialization.json.Json

/**
 * JSON-based remote feature source.
 *
 * Placeholder for future Ktor / HTTP implementation.
 */

internal class RemoteFeatureDataSourceImpl(
    private val jsonProvider: RemoteFeatureJsonProvider,
    private val json: Json
) : RemoteFeatureDataSource {

    override fun fetch(identity: ClientIdentity): FeatureSnapshot? {
        val raw = jsonProvider.load()
        return json.decodeFromString(raw)
    }
}