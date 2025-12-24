package dev.igordesouza.shortenurlapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteAllUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteUrlUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.GetRecentlyShortenedUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.ShortenUrlUseCase
import dev.igordesouza.shortenurlapp.presentation.home.model.Url
import dev.igordesouza.shortenurlapp.presentation.home.model.mapper.toDomain
import dev.igordesouza.shortenurlapp.presentation.home.model.mapper.toPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModelImpl(
    private val shortenUrlUseCase: ShortenUrlUseCase,
    private val getRecentlyShortenedUrlsUseCase: GetRecentlyShortenedUrlsUseCase,
    private val deleteUrlUseCase: DeleteUrlUseCase,
    private val deleteAllUrlsUseCase: DeleteAllUrlsUseCase
) : ViewModel(), HomeViewModel {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    override val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    override fun getRecentlyShortenedUrls() {
        viewModelScope.launch {
            try {
                val urls = getRecentlyShortenedUrlsUseCase().map { it.toPresentation() }
                _uiState.update { currentState ->
                    val newUrlInput =
                        if (currentState is HomeUiState.Shortening) ""
                        else currentState.urlInput
                    HomeUiState.Idle(urls = urls, urlInput = newUrlInput)
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    HomeUiState.Idle(
                        urls = currentState.urls,
                        urlInput = currentState.urlInput,
                        error = e.message
                    )
                }
            }
        }
    }

    override fun onUrlInputChanged(input: String) {
        _uiState.update { currentState ->
            (currentState as? HomeUiState.Idle)?.copy(urlInput = input) ?: currentState
        }
    }

    override fun shortenUrl() {
        val currentState = _uiState.value
        if (currentState.urlInput.isBlank()) {
            return
        }

        _uiState.update {
            HomeUiState.Shortening(urls = it.urls, urlInput = it.urlInput)
        }

        shortenUrlUseCase(currentState.urlInput).onEach { result ->
            result.onSuccess {
                getRecentlyShortenedUrls()
            }
            result.onFailure { error ->
                _uiState.update { stateInUpdate ->
                    HomeUiState.Idle(
                        urls = stateInUpdate.urls,
                        urlInput = stateInUpdate.urlInput,
                        error = "Failed to shorten URL: ${error.message}"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun deleteUrl(url: Url) {
        viewModelScope.launch {
            deleteUrlUseCase(url.toDomain())
            getRecentlyShortenedUrls()
        }
    }

    override fun deleteAllUrls() {
        viewModelScope.launch {
            deleteAllUrlsUseCase()
            getRecentlyShortenedUrls()
        }
    }

    override fun dismissError() {
        _uiState.update { currentState ->
            (currentState as? HomeUiState.Idle)?.copy(error = null) ?: currentState
        }
    }
}
