package dev.igordesouza.orthos.core.verdict

import dev.igordesouza.orthos.core.signal.SignalResult


/**
 * Final verdict returned by Orthos.
 *
 * @property state Evaluated runtime state.
 * @property score Score of the verdict. Just for demo purposes, check actual utility in production.
 * @property results Evaluated results. Just for demo purposes, check actual utility in production.
 * @property timestamp Evaluation time.
 *
 * */
data class OrthosVerdict(
    val state: RuntimeState,
    val score: Int,
    val results: List<SignalResult>,
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {

        fun OrthosVerdict.Companion.safe(): OrthosVerdict =
            OrthosVerdict(
                state = RuntimeState.SAFE,
                score = 0,
                results = emptyList()
            )

        fun OrthosVerdict.Companion.tampered(
            scoreBoost: Int,
            results: List<SignalResult>
        ): OrthosVerdict =
            OrthosVerdict(
                state = RuntimeState.TAMPERED,
                score = scoreBoost,
                results = results
            )
    }
}

