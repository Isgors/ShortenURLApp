package dev.igordesouza.shortenurlapp.presentation.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.igordesouza.orthos.runtime.OrthosRuntime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrthosVerdictViewModel(
    private val orthosRuntime: OrthosRuntime
) : ViewModel() {

    private val _state = MutableStateFlow(OrthosVerdictUiState())
    val state: StateFlow<OrthosVerdictUiState> = _state

    init {
        viewModelScope.launch {
            val verdict = orthosRuntime.evaluate()
            _state.value = OrthosVerdictUiState(false, verdict)
        }
    }
}