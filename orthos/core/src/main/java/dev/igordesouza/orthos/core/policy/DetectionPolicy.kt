package dev.igordesouza.orthos.core.policy

import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.core.signal.SignalResult

/**
 * Strategy interface for evaluating signal results.
 */
interface DetectionPolicy {

    /**
     * Evaluates the collected signal results and returns a RuntimeState.
     */
    fun evaluate(results: List<SignalResult>): OrthosVerdict
}
