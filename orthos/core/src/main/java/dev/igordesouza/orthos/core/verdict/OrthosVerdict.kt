package dev.igordesouza.orthos.core.verdict

import dev.igordesouza.orthos.core.signal.SignalResult


/**
 * Final verdict returned by Orthos.
 *
 * @property state Evaluated runtime state.
 * @property score Computed score based on evidences. Just for demo purposes, check actual utility in production.
 * @property evidences Only signals that effectively triggered. Just for demo purposes, check actual utility in production.
 * @property timestamp Evaluation time.
 *
 * */
data class OrthosVerdict(
    val state: RuntimeState,
    val score: Int,
    val evidences: List<SignalResult>,
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {

        fun OrthosVerdict.Companion.safe(): OrthosVerdict =
            OrthosVerdict(
                state = RuntimeState.SAFE,
                score = 0,
                evidences = emptyList()
            )

        fun OrthosVerdict.Companion.tampered(
            scoreBoost: Int,
            results: List<SignalResult>
        ): OrthosVerdict =
            OrthosVerdict(
                state = RuntimeState.TAMPERED,
                score = scoreBoost,
                evidences = results
            )
    }
}

