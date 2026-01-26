package dev.igordesouza.shortenurlapp.domain.model

sealed interface ShortenUrlOutcome {
    data object EmptyInput : ShortenUrlOutcome
    data object Success : ShortenUrlOutcome
    data class Error(val message: String) : ShortenUrlOutcome
}