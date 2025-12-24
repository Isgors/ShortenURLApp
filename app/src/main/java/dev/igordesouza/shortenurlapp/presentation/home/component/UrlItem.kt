package dev.igordesouza.shortenurlapp.presentation.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.igordesouza.shortenurlapp.presentation.home.model.Url
import dev.igordesouza.shortenurlapp.presentation.theme.ShortenURLAppTheme

@Composable
fun UrlItem(
    url: Url,
    isDeleteEnabled: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
            .testTag("UrlItem_${url.alias}")
            .semantics {
                contentDescription = "Shortened URL item"
            },
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

        IconButton(
            enabled = isDeleteEnabled,
            onClick = onDelete,
            modifier = Modifier.testTag("DeleteUrl_${url.alias}")
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete URL"
            )
        }
    }
}

@Preview
@Composable
private fun UrlItemPreview() {
    ShortenURLAppTheme {
        Surface {
            UrlItem(
                Url("1", "https://google.com", "short.ly/1"),
                isDeleteEnabled = true,
                onClick = {},
                onDelete = {}
            )
        }
    }
}