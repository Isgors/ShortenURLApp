package dev.igordesouza.orthos.core.policy

import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.core.verdict.RuntimeState
import dev.igordesouza.orthos.core.signal.SignalResult

/**
 * Graded policy:
 * - Accumulates confidence scores
 * - Allows partial degradation (SUSPICIOUS).
 *
 */

class GradedDetectionPolicy(
    private val suspiciousThreshold: Int,
    private val tamperedThreshold: Int
) : DetectionPolicy {

    override fun evaluate(results: List<SignalResult>): OrthosVerdict {
        val score = results
            .filter { it.triggered }
            .sumOf { (it.confidence * 100).toInt() }

        val state = when {
            score >= tamperedThreshold -> RuntimeState.TAMPERED
            score >= suspiciousThreshold -> RuntimeState.SUSPICIOUS
            else -> RuntimeState.SAFE
        }

        return OrthosVerdict(state, score, results)
    }
}
