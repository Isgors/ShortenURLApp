package dev.igordesouza.orthos.runtime.policy

import dev.igordesouza.orthos.core.policy.DetectionPolicy

/**
 * Provides the active DetectionPolicy at runtime.
 *
 * This abstraction enables policy hot-swap without
 * requiring a new app build.
 */
interface PolicyProvider {

    /**
     * @return the currently active policy or null
     *         to fallback to snapshot policy.
     */
    fun current(): DetectionPolicy?
}
