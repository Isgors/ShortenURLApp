package dev.igordesouza.orthos.runtime.policy

import dev.igordesouza.orthos.core.policy.DetectionPolicy
import dev.igordesouza.orthos.core.signal.SignalResult
import dev.igordesouza.orthos.core.verdict.OrthosVerdict

/**
 * Central runtime decision engine.
 *
 * Responsibilities:
 * - Enforce fail-safe behavior
 *
 * This class is intentionally stateful and runtime-aware.
 */
class PolicyEvaluator(
    private val failSafeHandler: FailSafeHandler
) {

    /**
     * Evaluates the final verdict based on signal results and policies.
     *
     * @param policy policy resolved from FeatureSnapshot
     * @param results results produced by SignalExecutor
     */
    fun evaluate(
        policy: DetectionPolicy,
        results: List<SignalResult>
    ): OrthosVerdict {

        return try {
            policy.evaluate(results)
        } catch (t: Throwable) {
            // 5️⃣ Fail-safe behavior
            failSafeHandler.onFailure(t)
        }
    }
}