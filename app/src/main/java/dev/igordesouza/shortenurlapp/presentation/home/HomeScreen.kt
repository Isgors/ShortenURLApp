package dev.igordesouza.shortenurlapp.presentation.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.igordesouza.shortenurlapp.presentation.home.component.UrlActionsBottomSheet
import dev.igordesouza.shortenurlapp.presentation.home.model.Url

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    var bottomSheetUrl by remember { mutableStateOf<Url?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {

                is HomeEffect.ShowError ->
                    snackbarHostState.showSnackbar(effect.message)

                is HomeEffect.ShowUrlActions ->
                    bottomSheetUrl = effect.url

                is HomeEffect.CopyUrl -> {
                    copyToClipboard(
                        context = context,
                        label = "Shortened URL",
                        text = effect.url.shortenedUrl
                    )
                    snackbarHostState.showSnackbar("URL copied")
                    bottomSheetUrl = null
                }

                is HomeEffect.OpenUrl -> {
                    openInBrowser(context, effect.url.shortenedUrl)
                    bottomSheetUrl = null
                }

                is HomeEffect.ScrollToIndex ->
                    listState.animateScrollToItem(effect.index)
            }
        }
    }

    bottomSheetUrl?.let { url ->
        UrlActionsBottomSheet(
            url = url,
            onCopyClick = {
                viewModel.dispatch(HomeIntent.CopyUrlClicked(url))
            },
            onOpenClick = {
                viewModel.dispatch(HomeIntent.OpenUrlClicked(url))
            },
            onDismiss = { bottomSheetUrl = null }
        )
    }

    HomeContent(
        state = state,
        listState = listState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::dispatch
    )
}

fun copyToClipboard(
    context: Context,
    label: String,
    text: String
) {
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

fun openInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

