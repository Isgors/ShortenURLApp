package dev.igordesouza.orthos.runtime.policy

import dev.igordesouza.orthos.core.policy.DetectionPolicy

/**
 * Default policy provider that always returns
 * the policy resolved from the FeatureSnapshot.
 */
class SnapshotPolicyProvider(
    private val snapshotPolicy: DetectionPolicy
) : PolicyProvider {

    override fun current(): DetectionPolicy = snapshotPolicy
}
