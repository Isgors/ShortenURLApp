package dev.igordesouza.shortenurlapp.domain.model

sealed interface ShortenUrlOutcome {
    data object EmptyInput : ShortenUrlOutcome
    data class Success(val url: Url) : ShortenUrlOutcome
    data class Error(val message: String) : ShortenUrlOutcome
}