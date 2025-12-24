package dev.igordesouza.shortenurlapp.presentation.home

import dev.igordesouza.shortenurlapp.presentation.home.model.Url

sealed interface HomeUiState {
    val urls: List<Url>
    val urlInput: String

    data object Loading : HomeUiState {
        override val urls: List<Url> = emptyList()
        override val urlInput: String = ""
    }

    data class Idle(
        override val urls: List<Url> = emptyList(),
        override val urlInput: String = "",
        val error: String? = null,
    ) : HomeUiState

    data class Shortening(
        override val urls: List<Url>,
        override val urlInput: String,
    ) : HomeUiState
}
