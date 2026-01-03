package dev.igordesouza.shortenurlapp.presentation.security

import androidx.activity.compose.LocalActivity
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

@Composable
fun OrthosVerdictScreen(
    onContinue: () -> Unit,
    viewModel: OrthosVerdictViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val activity = LocalActivity.current

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val verdict = state.verdict ?: return

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Verdict: ${verdict.state}", style = MaterialTheme.typography.headlineMedium)
        Text("Score: ${verdict.score}")

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(verdict.results.size) {
                Text("• ${verdict.results[it].signalId}")
            }
        }

        val (text, color, action) = when (verdict.state) {
            RuntimeState.SAFE -> Triple("Continue", Color.Green, onContinue)
            RuntimeState.SUSPICIOUS -> Triple("Continue anyway", Color.Yellow, onContinue)
            RuntimeState.TAMPERED -> Triple("Close app", Color.Red) {
                activity?.finishAffinity()
            }
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