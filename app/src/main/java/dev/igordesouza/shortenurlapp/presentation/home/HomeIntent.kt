package dev.igordesouza.shortenurlapp.presentation.home

import dev.igordesouza.shortenurlapp.presentation.home.model.Url

sealed interface HomeIntent {

    data class UrlInputChanged(val value: String) : HomeIntent
    data object ShortenClicked : HomeIntent

    data class UrlClicked(val url: Url) : HomeIntent

    data class DeleteUrlRequested(val url: Url) : HomeIntent
    data object DeleteAllRequested : HomeIntent

    data class ConfirmDeleteUrl(val url: Url) : HomeIntent
    data object ConfirmDeleteAll : HomeIntent

    data object DismissDialogs : HomeIntent

    data class CopyUrlClicked(val url: Url) : HomeIntent

    data class OpenUrlClicked(val url: Url) : HomeIntent
}
