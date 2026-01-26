package dev.igordesouza.shortenurlapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.igordesouza.shortenurlapp.domain.model.ShortenUrlOutcome
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteAllUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.DeleteUrlUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.ObserveUrlsUseCase
import dev.igordesouza.shortenurlapp.domain.usecase.ShortenUrlUseCase
import dev.igordesouza.shortenurlapp.presentation.home.HomeEffect.*
import dev.igordesouza.shortenurlapp.presentation.home.model.toDomain
import dev.igordesouza.shortenurlapp.presentation.home.model.toPresentation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val observeUrlsUseCase: ObserveUrlsUseCase,
    private val shortenUrlUseCase: ShortenUrlUseCase,
    private val deleteUrlUseCase: DeleteUrlUseCase,
    private val deleteAllUrlsUseCase: DeleteAllUrlsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private val _effect = Channel<HomeEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeUrlsUseCase()
            .onEach { urls ->
                // TODO handle empty list
                _state.update { it.copy(url = urls.toPresentation()[0]) }
            }
            .launchIn(viewModelScope)
    }

    fun dispatch(intent: HomeIntent) {
        when (intent) {

            is HomeIntent.UrlInputChanged ->
                _state.update { it.copy(urlInput = intent.value) }

            HomeIntent.ShortenClicked -> shortenUrl()

            is HomeIntent.UrlClicked ->
                sendEffect(ShowUrlActions(intent.url))

            is HomeIntent.CopyUrlClicked ->
                sendEffect(CopyUrl(intent.url))

            is HomeIntent.OpenUrlClicked ->
                sendEffect(OpenUrl(intent.url))

            is HomeIntent.ConfirmDeleteUrl -> {
                viewModelScope.launch {
                    deleteUrlUseCase(intent.url.toDomain())
                    _state.update { it.copy(urlToDelete = null) }
                }
            }

            HomeIntent.ConfirmDeleteAll -> {
                viewModelScope.launch {
                    deleteAllUrlsUseCase()
                    _state.update { it.copy(showDeleteAllDialog = false) }
                }
            }
            
            is HomeIntent.DeleteUrlRequested ->
                _state.update { it.copy(urlToDelete = intent.url) }

            is HomeIntent.DeleteAllRequested ->
                _state.update { it.copy(showDeleteAllDialog = true) }

            is HomeIntent.DismissDialogs ->
                _state.update {
                    it.copy(
                        urlToDelete = null,
                        showDeleteAllDialog = false
                    )
                }
        }
    }

    private fun shortenUrl() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        shortenUrlUseCase(state.value.urlInput)
            .onCompletion {
                _state.update { it.copy(isLoading = false) }
            }
            .collect { outcome ->
                when (outcome) {
                    ShortenUrlOutcome.EmptyInput ->
                        sendEffect(ShowError("URL cannot be empty"))

                    is ShortenUrlOutcome.Success -> {
                        _state.update {
                            it.copy(
                                urlInput = "",
                                url = outcome.url.toPresentation()
                            )
                        }
                        sendEffect(ScrollToIndex(0))
                    }

                    is ShortenUrlOutcome.Error ->
                        sendEffect(ShowError(outcome.message))
                }
            }
    }

    private fun sendEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
