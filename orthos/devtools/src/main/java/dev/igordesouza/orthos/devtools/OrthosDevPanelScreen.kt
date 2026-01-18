package dev.igordesouza.orthos.devtools

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrthosDevPanelScreen() {
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val viewModel: OrthosDevPanelViewModel = viewModel(
        factory = OrthosDevPanelViewModelFactory(context)
    )

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSnapshotIntoEditor()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Orthos Dev Panel 🔧") }) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { paddingValues ->
        OrthosDevPanelContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onEditorChange = viewModel::onEditorChange,
            onLoad = {
                viewModel.loadSnapshotIntoEditor()
                scope.launch { snackbar.showSnackbar("Loaded") }
            },
            onApply = {
                viewModel.applySnapshot { ok ->
                    scope.launch {
                        snackbar.showSnackbar(
                            if (ok) "Applied ✅ (Orthos.reset). Restart process to re-evaluate."
                            else "Invalid JSON ❌"
                        )
                    }
                }
            },
            onResetSnapshot = {
                viewModel.resetSnapshot {
                    scope.launch { snackbar.showSnackbar("Reset local JSON override") }
                }
            },
            onForceOn = {
                viewModel.forceOn {
                    scope.launch { snackbar.showSnackbar("Forced: ON ✅ (override)") }
                }
            },
            onForceOff = {
                viewModel.forceOff {
                    scope.launch { snackbar.showSnackbar("Forced: OFF ⛔ (override)") }
                }
            },
            onClearForce = {
                viewModel.clearForce {
                    scope.launch { snackbar.showSnackbar("Override cleared ↩️ (back to consumer flag)") }
                }
            }
        )
    }
}
