package dev.igordesouza.shortenurlapp.presentation.home

import dev.igordesouza.shortenurlapp.presentation.home.model.Url
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {

    val uiState: StateFlow<HomeUiState>

    fun getRecentlyShortenedUrls()

    fun onUrlInputChanged(input: String)

    fun shortenUrl()

    fun deleteUrl(url: Url)

    fun deleteAllUrls()

    fun dismissError()
}
