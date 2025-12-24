package dev.igordesouza.shortenurlapp.presentation.home

import dev.igordesouza.shortenurlapp.presentation.home.model.Url

sealed interface HomeEffect {
    /** UI feedback */
    data class ShowError(val message: String) : HomeEffect
    data class ShowUrlActions(val url: Url) : HomeEffect

    /** One-shot system actions */
    data class CopyUrl(val url: Url) : HomeEffect
    data class OpenUrl(val url: Url) : HomeEffect

    /** UI control */
    data class ScrollToIndex(val index: Int) : HomeEffect
}