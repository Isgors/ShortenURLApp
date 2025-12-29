package dev.igordesouza.orthos.core.policy

import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.core.verdict.RuntimeState
import dev.igordesouza.orthos.core.signal.SignalResult

/**
 * Strict policy:
 *
 * - Any triggered signal immediately compromises the runtime.
 * - Recommended for high-security applications.
 */

class StrictDetectionPolicy(
    private val threshold: Int
) : DetectionPolicy {

    override fun evaluate(results: List<SignalResult>): OrthosVerdict {
        val score = results
            .filter { it.triggered }
            .sumOf { (it.confidence * threshold).toInt() }

        return OrthosVerdict(
            state = if (score > 0) RuntimeState.TAMPERED else RuntimeState.SAFE,
            score = score,
            results = results
        )
    }
}