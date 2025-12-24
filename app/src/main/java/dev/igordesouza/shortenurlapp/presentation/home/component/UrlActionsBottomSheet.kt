package dev.igordesouza.shortenurlapp.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.igordesouza.shortenurlapp.presentation.home.model.Url
import dev.igordesouza.shortenurlapp.presentation.theme.ShortenURLAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlActionsBottomSheet(
    url: Url,
    onCopyClick: () -> Unit,
    onOpenClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Url alias: ${url.alias}",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("OpenUrlButton"),
                onClick = onOpenClick
            ) {
                Text("Open in browser")
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("CopyUrlButton"),
                onClick = onCopyClick
            ) {
                Text("Copy URL")
            }
        }
    }
}

@Preview
@Composable
private fun UrlActionsBottomSheetPreview() {
    ShortenURLAppTheme {
        UrlActionsBottomSheet(
            url = Url("1", "https://google.com", "short.ly/1"),
            onCopyClick = {},
            onOpenClick = {},
            onDismiss = {}
        )
    }
}