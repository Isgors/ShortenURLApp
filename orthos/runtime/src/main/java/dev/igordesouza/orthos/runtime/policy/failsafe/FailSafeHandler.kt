package dev.igordesouza.orthos.runtime.policy.failsafe

import dev.igordesouza.orthos.runtime.policy.dsl.PolicyDefinition

/**
 * Defines how the runtime behaves when an unexpected
 * error occurs during evaluation.
 */
interface FailSafeHandler {

    /**
     * @param cause of the failure
     */
    fun fallback(cause: Throwable?): PolicyDefinition
}
