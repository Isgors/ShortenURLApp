package dev.igordesouza.orthos.runtime.policy

import dev.igordesouza.orthos.core.verdict.RuntimeState
import dev.igordesouza.orthos.runtime.policy.dsl.PolicyDefinition
import dev.igordesouza.orthos.runtime.policy.dsl.SumScoreStrategy
import dev.igordesouza.orthos.runtime.policy.dsl.policy
import dev.igordesouza.orthos.runtime.policy.failsafe.FailSafeHandler


class PolicyDslFactory(
    private val failSafeHandler: FailSafeHandler
) {

    fun buildPolicyDefinition(policy: Policy?): PolicyDefinition =
        try {
            if (policy == null) {
                failSafeHandler.fallback(null)
            } else {
                build(policy)
            }
        } catch (t: Throwable) {
            failSafeHandler.fallback(t)
        }

    private fun build(policy: Policy): PolicyDefinition =
        when (policy.type) {

            PolicyType.STRICT -> {
                val threshold = requireNotNull(policy.tamperedThreshold) {
                    "STRICT policy requires threshold"
                }

                policy {
                    score(SumScoreStrategy)

                    whenScore {
                        atLeast(threshold, RuntimeState.TAMPERED)
                        otherwise then RuntimeState.SAFE
                    }
                }
            }

            PolicyType.GRADED -> {
                val suspicious = requireNotNull(policy.suspiciousThreshold) {
                    "GRADED policy requires suspiciousThreshold"
                }
                val tampered = requireNotNull(policy.tamperedThreshold) {
                    "GRADED policy requires tamperedThreshold"
                }

                policy {
                    score(SumScoreStrategy)

                    whenScore {
                        atLeast(tampered, RuntimeState.TAMPERED)
                        atLeast(suspicious, RuntimeState.SUSPICIOUS)
                        otherwise then RuntimeState.SAFE
                    }
                }
            }
        }
}
