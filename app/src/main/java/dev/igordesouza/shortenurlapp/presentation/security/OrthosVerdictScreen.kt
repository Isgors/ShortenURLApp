package dev.igordesouza.shortenurlapp.presentation.security

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.igordesouza.orthos.core.verdict.RuntimeState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrthosVerdictScreen(
    onContinue: () -> Unit,
    onExit: () -> Unit,
    viewModel: OrthosVerdictViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val verdict = state.verdict ?: return

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("URL Shortener") })
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Verdict: ${verdict.state}", style = MaterialTheme.typography.headlineMedium)
            Text("Score: ${verdict.score}")

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(verdict.evidences.size) {
                    Text("• ${verdict.evidences[it].signalId}")
                }
            }

            val (text, color, action) = when (verdict.state) {
                RuntimeState.SAFE -> Triple("Continue", Color.Green, onContinue)
                RuntimeState.SUSPICIOUS -> Triple("Continue anyway", Color.Yellow, onContinue)
                RuntimeState.TAMPERED -> Triple("Close app", Color.Red, onExit)
            }

            Button(
                onClick = { action.invoke() },
                colors = ButtonDefaults.buttonColors(containerColor = color),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text)
            }
        }

    }
}