package dev.igordesouza.orthos.runtime.policy.failsafe

import dev.igordesouza.orthos.core.verdict.RuntimeState
import dev.igordesouza.orthos.runtime.policy.dsl.SumScoreStrategy
import dev.igordesouza.orthos.runtime.policy.dsl.policy

class PermissiveFailSafeHandler : FailSafeHandler {

    override fun fallback(cause: Throwable?) =
        policy {
            score(SumScoreStrategy)

            whenScore {
                otherwise then RuntimeState.SAFE
            }
        }
}
