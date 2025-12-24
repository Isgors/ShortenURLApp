package dev.igordesouza.shortenurlapp.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.getRecentlyShortenedUrls()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("URL Shortener") }
            )
                 },
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        val currentUiState = uiState
        if (currentUiState is HomeUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.testTag("LoadingIndicator")
                )
            }
        } else {
            HomeContent(
                urls = currentUiState.urls,
                urlInput = currentUiState.urlInput,
                isLoading = currentUiState is HomeUiState.Shortening,
                onUrlInputChange = viewModel::onUrlInputChanged,
                onShortenUrl = viewModel::shortenUrl,
                onDeleteUrl = viewModel::deleteUrl,
                onDeleteAllUrls = viewModel::deleteAllUrls,
                contentPadding = paddingValues
            )
        }

        val error by remember { derivedStateOf { (uiState as? HomeUiState.Idle)?.error } }
        if (error != null) {
            LaunchedEffect(error) {
                snackbarHostState.showSnackbar(
                    message = error!!,
                    duration = SnackbarDuration.Short
                )
                viewModel.dismissError()
            }
        }
    }
}
