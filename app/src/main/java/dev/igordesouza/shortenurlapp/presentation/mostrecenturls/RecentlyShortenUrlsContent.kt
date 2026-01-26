package dev.igordesouza.shortenurlapp.presentation.mostrecenturls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.igordesouza.shortenurlapp.presentation.home.HomeIntent
import dev.igordesouza.shortenurlapp.presentation.home.HomeState
import dev.igordesouza.shortenurlapp.presentation.home.component.UrlItem
import dev.igordesouza.shortenurlapp.presentation.home.model.Url
import dev.igordesouza.shortenurlapp.presentation.theme.ShortenURLAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentlyShortenUrlsContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        content = {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Shorten URL"
                            )
                        },
                        onClick = {  }
                    )
                },
                title = {}
            )
        },
        snackbarHost = { SnackbarHost(
            modifier = Modifier.testTag("SnackbarHost"),
            hostState = snackbarHostState
        ) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        Column {
            // HEADER
            Row(
                modifier = Modifier.padding(paddingValues).fillMaxWidth(),
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

            LazyColumn(
                state = listState
            ) {
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

@Preview
@Composable
private fun RecentlyShortenUrlsContentEmptyPreview() {
    ShortenURLAppTheme {
        RecentlyShortenUrlsContent(
            state = HomeState(
                urls = emptyList(),
                urlInput = ""
            ),
            onIntent = {}
        )
    }
}

@Preview
@Composable
private fun RecentlyShortenUrlsContentPreview() {
    ShortenURLAppTheme {
        RecentlyShortenUrlsContent(
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
}