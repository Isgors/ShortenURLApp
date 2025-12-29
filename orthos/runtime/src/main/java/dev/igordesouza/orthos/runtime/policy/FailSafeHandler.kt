package dev.igordesouza.orthos.runtime.policy

import dev.igordesouza.orthos.core.verdict.OrthosVerdict

/**
 * Defines how the runtime behaves when an unexpected
 * error occurs during evaluation.
 */
interface FailSafeHandler {

    /**
     * @param throwable the failure cause
     */
    fun onFailure(throwable: Throwable): OrthosVerdict
}
