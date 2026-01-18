package dev.igordesouza.orthos.devtools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OrthosDevPanelContent(
    state: OrthosDevPanelUiState,
    onEditorChange: (String) -> Unit,
    onLoad: () -> Unit,
    onApply: () -> Unit,
    onResetSnapshot: () -> Unit,
    onForceOn: () -> Unit,
    onForceOff: () -> Unit,
    onClearForce: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("Gate override (wins over consumer BuildConfig flag):")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = onForceOn) { Text("Force ON") }
            Button(onClick = onForceOff) { Text("Force OFF") }
            Button(onClick = onClearForce) { Text("Clear") }
        }

        Text("Current override: ${forceModeLabel(state.forceMode)}")

        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            OutlinedTextField(
                value = state.editorText,
                onValueChange = onEditorChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                label = { Text("FeatureSnapshot JSON") }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = onLoad) { Text("Load") }
            Button(onClick = onApply) { Text("Apply") }
            Button(onClick = onResetSnapshot) { Text("Reset JSON") }
        }
    }
}

private fun forceModeLabel(mode: Int): String = when (mode) {
    OrthosDevTools.MODE_FORCE_ON -> "FORCE_ON"
    OrthosDevTools.MODE_FORCE_OFF -> "FORCE_OFF"
    else -> "DEFAULT"
}

// ──────────────────────────────
// Previews
// ──────────────────────────────

@Preview(name = "Loading", showBackground = true)
@Composable
private fun Preview_OrthosDevPanel_Loading() {
    OrthosDevPanelContent(
        state = OrthosDevPanelUiState(
            isLoading = true,
            editorText = "// Loading...\n{}",
            forceMode = OrthosDevTools.MODE_DEFAULT
        ),
        onEditorChange = {},
        onLoad = {},
        onApply = {},
        onResetSnapshot = {},
        onForceOn = {},
        onForceOff = {},
        onClearForce = {}
    )
}

@Preview(name = "Default + JSON", showBackground = true)
@Composable
private fun Preview_OrthosDevPanel_Default() {
    OrthosDevPanelContent(
        state = OrthosDevPanelUiState(
            isLoading = false,
            editorText = "{\n  \"policy\": {\"type\": \"STRICT\", \"threshold\": 100}\n}",
            forceMode = OrthosDevTools.MODE_DEFAULT
        ),
        onEditorChange = {},
        onLoad = {},
        onApply = {},
        onResetSnapshot = {},
        onForceOn = {},
        onForceOff = {},
        onClearForce = {}
    )
}

@Preview(name = "Forced ON", showBackground = true)
@Composable
private fun Preview_OrthosDevPanel_ForcedOn() {
    OrthosDevPanelContent(
        state = OrthosDevPanelUiState(
            isLoading = false,
            editorText = "{\n  \"signals\": {\"EMULATOR\": {\"enabled\": true}}\n}",
            forceMode = OrthosDevTools.MODE_FORCE_ON
        ),
        onEditorChange = {},
        onLoad = {},
        onApply = {},
        onResetSnapshot = {},
        onForceOn = {},
        onForceOff = {},
        onClearForce = {}
    )
}

@Preview(name = "Forced OFF", showBackground = true)
@Composable
private fun Preview_OrthosDevPanel_ForcedOff() {
    OrthosDevPanelContent(
        state = OrthosDevPanelUiState(
            isLoading = false,
            editorText = "{\n  \"signals\": {\"BYTECODE_CANARY\": {\"enabled\": true}}\n}",
            forceMode = OrthosDevTools.MODE_FORCE_OFF
        ),
        onEditorChange = {},
        onLoad = {},
        onApply = {},
        onResetSnapshot = {},
        onForceOn = {},
        onForceOff = {},
        onClearForce = {}
    )
}
