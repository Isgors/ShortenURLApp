package dev.igordesouza.shortenurlapp.presentation.security

import dev.igordesouza.orthos.core.verdict.OrthosVerdict
import dev.igordesouza.orthos.core.verdict.RuntimeState

data class OrthosVerdictUiState(
    val isLoading: Boolean = true,
    val verdict: OrthosVerdict? = null
) {
    val state: RuntimeState? get() = verdict?.state
}