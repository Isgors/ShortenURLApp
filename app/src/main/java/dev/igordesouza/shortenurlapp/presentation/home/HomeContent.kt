package dev.igordesouza.shortenurlapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.igordesouza.shortenurlapp.presentation.component.dialog.DecisionDialog
import dev.igordesouza.shortenurlapp.presentation.home.component.UrlItem
import dev.igordesouza.shortenurlapp.presentation.home.model.Url

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("URL Shortener") })
        },
        snackbarHost = { SnackbarHost(
            modifier = Modifier.testTag("SnackbarHost"),
            hostState = snackbarHostState
        ) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // INPUT ROW
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .testTag("UrlInput")
                    ,
                    placeholder = { Text("Enter URL to shorten") },
                    value = state.urlInput,
                    onValueChange = {
                        onIntent(HomeIntent.UrlInputChanged(it))
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        keyboardController?.hide()
                        onIntent(HomeIntent.ShortenClicked)
                    }),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                    enabled = !state.isLoading
                )

                IconButton(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .testTag("ShortenButton"),
                    enabled = state.urlInput.isNotBlank() && !state.isLoading,
                    onClick = {
                        keyboardController?.hide()
                        onIntent(HomeIntent.ShortenClicked)
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Shorten URL"
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // LOADING
            if (state.isLoading) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag("ShorteningUrlIndicator")
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Shortening your URL...",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            } else {

                // HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recently shortened URLs",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        modifier = Modifier.testTag("DeleteAllButton"),
                        enabled = state.urls.isNotEmpty(),
                        onClick = {
                            onIntent(HomeIntent.DeleteAllRequested)
                        }
                    ) {
                        Text("Delete All")
                    }
                }

                Spacer(Modifier.height(8.dp))

                if (state.urls.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No shortened URLs yet",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    LazyColumn(state = listState) {
                        items(
                            items = state.urls,
                            key = { it.alias }
                        ) { url ->
                            UrlItem(
                                modifier = Modifier.testTag("UrlItem_${url.alias}"),
                                url = url,
                                isDeleteEnabled = true,
                                onDelete = {
                                    onIntent(HomeIntent.DeleteUrlRequested(url))
                                },
                                onClick = {
                                    onIntent(HomeIntent.UrlClicked(url))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // DELETE ALL DIALOG
    if (state.showDeleteAllDialog) {
        DecisionDialog(
            dialogTitle = "Delete All URLs",
            dialogText = "Are you sure you want to delete all your shortened URLs?",
            onDismissRequest = {
                onIntent(HomeIntent.DismissDialogs)
            },
            onConfirmation = {
                onIntent(HomeIntent.ConfirmDeleteAll)
            }
        )
    }

    // DELETE ONE DIALOG
    state.urlToDelete?.let { url ->
        DecisionDialog(
            dialogTitle = "Delete URL",
            dialogText = "Are you sure you want to delete this URL?",
            onDismissRequest = {
                onIntent(HomeIntent.DismissDialogs)
            },
            onConfirmation = {
                onIntent(HomeIntent.ConfirmDeleteUrl(url))
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    HomeContent(
        state = HomeState(
            urls = listOf(
                Url("1", "https://google.com", "short.ly/1"),
                Url("2", "https://android.com", "short.ly/2")
            ),
            urlInput = ""
        ),
        onIntent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun HomeContentLoadingPreview() {
    HomeContent(
        state = HomeState(
            isLoading = true,
            urlInput = "https://google.com"
        ),
        listState = rememberLazyListState(),
        onIntent = {}
    )
}
