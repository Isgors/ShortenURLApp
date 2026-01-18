package dev.igordesouza.orthos.devtools

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.igordesouza.orthos.runtime.feature.FeatureSnapshot
import dev.igordesouza.orthos.runtime.feature.datasource.LocalFeatureDataSourceImpl
import dev.igordesouza.orthos.sdk.Orthos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

data class OrthosDevPanelUiState(
    val isLoading: Boolean = true,
    val editorText: String = "// Loading...\n{}",
    /** 0=DEFAULT, 1=FORCE_ON, 2=FORCE_OFF (OrthosDevTools constants) */
    val forceMode: Int = OrthosDevTools.MODE_DEFAULT
)

class OrthosDevPanelViewModel(
    private val appContext: Context
) : ViewModel() {

//    private val context: Context = appContext.applicationContext

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val dataSource = LocalFeatureDataSourceImpl(appContext, json)

    // Local UI pieces
    private val _isLoading = MutableStateFlow(true)
    private val _editorText = MutableStateFlow("// Loading...\n{}")

    // force mode comes from DataStore (source of truth)
    private val forceModeFlow = OrthosDevTools.forceModeFlow(appContext)

    // Public UI state
    val state: StateFlow<OrthosDevPanelUiState> =
        combine(_isLoading, _editorText, forceModeFlow) { loading, text, mode ->
            OrthosDevPanelUiState(
                isLoading = loading,
                editorText = text,
                forceMode = mode
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = OrthosDevPanelUiState()
        )

    fun loadSnapshotIntoEditor() {
        viewModelScope.launch {
            _isLoading.value = true
            val snapshot = withContext(Dispatchers.IO) { dataSource.get() }

            val text = if (snapshot == null) {
                "// No local override found.\n// Paste a FeatureSnapshot JSON here and press Apply.\n{}"
            } else {
                json.encodeToString(FeatureSnapshot.serializer(), snapshot)
            }

            _editorText.value = text
            _isLoading.value = false
        }
    }

    fun onEditorChange(text: String) {
        _editorText.value = text
    }

    fun applySnapshot(onDone: (success: Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = runCatching {
                val snapshot = json.decodeFromString(FeatureSnapshot.serializer(), _editorText.value)
                withContext(Dispatchers.IO) { dataSource.save(snapshot) }
            }.isSuccess

            // Important: next Orthos.install(...) re-reads the gate + uses new snapshot
            if (ok) Orthos.reset()

            onDone(ok)
        }
    }

    fun resetSnapshot(onDone: () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { dataSource.clear() }
            Orthos.reset()
            loadSnapshotIntoEditor()
            onDone()
        }
    }

    fun setForceMode(mode: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            OrthosDevTools.setForceMode(appContext, mode)
            Orthos.reset()
            onDone()
        }
    }

    fun forceOn(onDone: () -> Unit) = setForceMode(OrthosDevTools.MODE_FORCE_ON, onDone)
    fun forceOff(onDone: () -> Unit) = setForceMode(OrthosDevTools.MODE_FORCE_OFF, onDone)
    fun clearForce(onDone: () -> Unit) = setForceMode(OrthosDevTools.MODE_DEFAULT, onDone)
}
