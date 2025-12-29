package dev.igordesouza.orthos.core.policy

import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.core.verdict.RuntimeState
import dev.igordesouza.orthos.core.signal.SignalResult

/**
 * Policy based on weighted scoring.
 */
class WeightedPolicy(
    private val threshold: Int
) : DetectionPolicy {

    override fun evaluate(results: List<SignalResult>): OrthosVerdict {
        val score = results.sumOf {
            if (it.triggered) (it.confidence * 100).toInt() else 0
        }

        val state = if (score >= threshold) {
            RuntimeState.TAMPERED
        } else {
            RuntimeState.SAFE
        }

        return OrthosVerdict(state, score, results)

    }
}
