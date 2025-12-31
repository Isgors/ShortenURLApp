package dev.igordesouza.orthos.runtime.feature.datasource

import android.content.Context

/**
 * Default implementation of [RemoteFeatureJsonProvider]
 * that loads the feature configuration from an asset file.
 *
 * This implementation is intended for:
 * - local development
 * - demos
 * - tests
 */
internal class RemoteFeatureJsonProviderImpl(
    private val context: Context,
    private val assetFileName: String = "orthos_features.json"
) : RemoteFeatureJsonProvider {

    override fun load(): String {
        return context.assets.open(assetFileName)
            .bufferedReader()
            .use { it.readText() }
    }
}
