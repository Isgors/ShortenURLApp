package dev.igordesouza.shortenurlapp.presentation.home

import dev.igordesouza.shortenurlapp.presentation.home.model.Url

data class HomeState(
    val url: Url? = null,
    val urlInput: String = "",
    val isLoading: Boolean = false,
    val urlToDelete: Url? = null
)
