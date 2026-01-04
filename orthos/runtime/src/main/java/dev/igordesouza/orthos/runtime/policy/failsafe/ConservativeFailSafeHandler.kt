package dev.igordesouza.orthos.runtime.policy.failsafe

import dev.igordesouza.orthos.core.verdict.RuntimeState
import dev.igordesouza.orthos.runtime.policy.dsl.SumScoreStrategy
import dev.igordesouza.orthos.runtime.policy.dsl.policy

/**
 * Conservative fail-safe:
 * Any failure results in a tampered verdict.
 */
class ConservativeFailSafeHandler : FailSafeHandler {

    override fun fallback(cause: Throwable?) =
        policy {
            score(SumScoreStrategy)

            whenScore {
                atLeast(1, RuntimeState.TAMPERED)
                otherwise then RuntimeState.SAFE
            }
        }
}
