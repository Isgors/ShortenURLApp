package dev.igordesouza.shortenurlapp.presentation.home

import dev.igordesouza.shortenurlapp.presentation.home.model.Url

data class HomeState(
    val urls: List<Url> = emptyList(),
    val urlInput: String = "",
    val isLoading: Boolean = false,
    val showDeleteAllDialog: Boolean = false,
    val urlToDelete: Url? = null
)
