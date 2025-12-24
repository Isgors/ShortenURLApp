package dev.igordesouza.shortenurlapp.presentation.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.igordesouza.shortenurlapp.presentation.home.model.Url
import kotlinx.coroutines.delay

@Composable
fun HomeContent(
    urls: List<Url>,
    urlInput: String,
    isLoading: Boolean,
    onUrlInputChange: (String) -> Unit,
    onShortenUrl: () -> Unit,
    onDeleteUrl: (Url) -> Unit,
    onDeleteAllUrls: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var previousUrls by remember { mutableStateOf(urls) }
    var newlyAddedUrl by remember { mutableStateOf<Url?>(null) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var urlToDeleteDialog by remember { mutableStateOf<Url?>(null) }
    var showLoadingIndicator by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(500)
            showLoadingIndicator = true
        } else {
            showLoadingIndicator = false
        }
    }

    LaunchedEffect(urls) {
        if (urls.size > previousUrls.size) {
            val newUrl = urls.first()
            newlyAddedUrl = newUrl
            listState.animateScrollToItem(0)
            keyboardController?.hide()
        }
        previousUrls = urls
    }

    if (showDeleteAllDialog) {
        DeleteConfirmationDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            onConfirmation = {
                onDeleteAllUrls()
                showDeleteAllDialog = false
            },
            dialogTitle = "Delete All URLs",
            dialogText = "Are you sure you want to delete all your shortened URLs? This action cannot be undone."
        )
    }

    urlToDeleteDialog?.let { url ->
        DeleteConfirmationDialog(
            onDismissRequest = { urlToDeleteDialog = null },
            onConfirmation = {
                onDeleteUrl(url)
                urlToDeleteDialog = null
            },
            dialogTitle = "Delete URL",
            dialogText = "Are you sure you want to delete this URL?"
        )
    }

    Column(
        modifier = Modifier
            .padding(contentPadding)
            .padding(16.dp)
    ) {
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
                value = urlInput,
                onValueChange = onUrlInputChange,
                shape = MaterialTheme.shapes.extraLarge,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    keyboardController?.hide()
                    onShortenUrl()
                }),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                enabled = !isLoading
            )
            IconButton(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .testTag("ShortenButton"),
                onClick = {
                    keyboardController?.hide()
                    onShortenUrl()
                },
                enabled = urlInput.isNotBlank() && !isLoading,
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Shorten URL"
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        if (showLoadingIndicator) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.testTag("ShorteningUrlIndicator")
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Shortening your URL...",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recently shortened URLs",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Button(
                    modifier = Modifier.testTag("DeleteAllButton"),
                    onClick = { showDeleteAllDialog = true },
                    enabled = urls.isNotEmpty() && !isLoading
                ) {
                    Text("Delete All")
                }
            }
            if (urls.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No shortened URLs yet",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                LazyColumn(state = listState) {
                    items(urls, key = { it.alias }) { url ->
                        var isNew by remember { mutableStateOf(false) }

                        LaunchedEffect(newlyAddedUrl) {
                            if (url.alias == newlyAddedUrl?.alias) {
                                isNew = true
                            }
                        }

                        val animatedColor by animateColorAsState(
                            targetValue = if (isNew) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.Transparent,
                            animationSpec = tween(durationMillis = 500)
                        )

                        LaunchedEffect(isNew) {
                            if (isNew) {
                                delay(1500) // Keep the highlight for a moment
                                isNew = false
                            }
                        }

                        UrlItem(
                            modifier = Modifier
                                .background(animatedColor)
                                .testTag("UrlItem_${url.alias}"),
                            url = url,
                            onDelete = { urlToDeleteDialog = url },
                            isDeleteEnabled = !isLoading
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun UrlItem(
    url: Url,
    onDelete: () -> Unit,
    isDeleteEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = url.alias,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = url.originalUrl,
                style = MaterialTheme.typography.titleSmall
            )
        }
        IconButton(onClick = onDelete, enabled = isDeleteEnabled) {
            Icon(Icons.Default.Delete, contentDescription = "Delete URL")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    HomeContent(
        urls = listOf(
            Url(
                "1536293812",
                "https://www.google.com",
                "https://url-shortener-server.onrender.com/api/alias/1536293812"
            ),
            Url("936366466", "https://www.android.com", "https://url-shortener-server.onrender.com/api/alias/936366466")
        ),
        urlInput = "",
        isLoading = false,
        onUrlInputChange = {},
        onShortenUrl = {},
        onDeleteUrl = {},
        onDeleteAllUrls = {}
    )
}

@Preview(showBackground = true)
@Composable
fun HomeContentEmptyPreview() {
    HomeContent(
        urls = emptyList(),
        urlInput = "",
        isLoading = false,
        onUrlInputChange = {},
        onShortenUrl = {},
        onDeleteUrl = {},
        onDeleteAllUrls = {}
    )
}

@Preview(showBackground = true)
@Composable
fun HomeContentLoadingPreview() {
    HomeContent(
            urls = emptyList(),
    urlInput = "https://www.google.com",
    isLoading = true,
    onUrlInputChange = {},
    onShortenUrl = {},
    onDeleteUrl = {},
    onDeleteAllUrls = {}
    )
}
