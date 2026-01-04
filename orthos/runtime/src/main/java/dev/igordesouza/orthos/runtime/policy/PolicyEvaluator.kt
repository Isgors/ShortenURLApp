package dev.igordesouza.orthos.runtime.policy

import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.runtime.policy.dsl.PolicyDefinition

/**
 * Central runtime decision engine.
 *
 * Responsibilities:
 * - Enforce fail-safe behavior
 *
 * This class is intentionally stateful and runtime-aware.
 */
class PolicyEvaluator {

    /**
     * Evaluates the final verdict based on signal results and policies.
     *
     * @param policy resolved from FeatureSnapshot
     * @param evidences produced by SignalExecutor
     */
    fun evaluate(
        policy: PolicyDefinition,
        evidences: List<SignalResult>
    ): OrthosVerdict {

        val score = policy.scoreStrategy.compute(evidences)

        val state = policy.rules
            .first { score >= it.minScore }
            .state

        return OrthosVerdict(
            state = state,
            score = score,
            evidences = evidences
        )
    }
}
