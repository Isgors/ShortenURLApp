package dev.igordesouza.orthos.runtime.policy.dsl

import dev.igordesouza.orthos.core.signal.SignalResult

/**
 * Strategy used to compute the final score.
 */
fun interface ScoreStrategy {
    fun compute(results: List<SignalResult>): Int
}
