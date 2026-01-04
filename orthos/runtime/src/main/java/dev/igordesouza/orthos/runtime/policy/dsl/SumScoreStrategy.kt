package dev.igordesouza.orthos.runtime.policy.dsl

import dev.igordesouza.orthos.core.signal.SignalResult

object SumScoreStrategy : ScoreStrategy {
    override fun compute(results: List<SignalResult>): Int =
        results.sumOf { it.confidence }.toInt()
}
