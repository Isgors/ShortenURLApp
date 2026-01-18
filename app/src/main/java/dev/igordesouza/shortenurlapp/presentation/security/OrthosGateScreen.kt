package dev.igordesouza.shortenurlapp.presentation.security

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

/**
 * Decides the real app entrypoint.
 *
 * If Orthos is disabled for this build variant, we skip straight to Home.
 * If enabled, we show the verdict screen.
 */
@Composable
fun OrthosGateScreen(
    onGoHome: () -> Unit,
    onShowVerdict: () -> Unit,
    viewModel: OrthosGateViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        if (!viewModel.isOrthosEnabled()) {
            onGoHome()
        } else {
            onShowVerdict()
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
