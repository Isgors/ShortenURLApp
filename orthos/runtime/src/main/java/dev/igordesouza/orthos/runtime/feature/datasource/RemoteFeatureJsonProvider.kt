package dev.igordesouza.orthos.runtime.feature.datasource

/**
 * Provides raw JSON for feature flags.
 *
 * This abstraction allows:
 * - assets
 * - raw resources
 * - hardcoded strings (tests)
 * - future HTTP responses
 */
fun interface RemoteFeatureJsonProvider {

    fun load(): String
}
