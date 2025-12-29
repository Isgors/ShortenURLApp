package dev.igordesouza.orthos.runtime.feature

import dev.igordesouza.orthos.core.feature.FeatureSnapshot
import dev.igordesouza.orthos.core.identity.ClientIdentity
import java.util.concurrent.atomic.AtomicReference

/**
 * Keeps a snapshot in memory to avoid
 * repeated repository access.
 */
class CachedFeatureSnapshotProvider(
    private val delegate: FeatureSnapshotProvider
) : FeatureSnapshotProvider {

    private val cache = AtomicReference<FeatureSnapshot?>()

    override fun get(identity: ClientIdentity): FeatureSnapshot {
        return cache.get() ?: delegate.get(identity).also {
            cache.set(it)
        }
    }
}
