package dev.igordesouza.shortenurlapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import dev.igordesouza.shortenurlapp.presentation.home.HomeScreen
import dev.igordesouza.shortenurlapp.presentation.mostrecenturls.RecentlyShortenUrlsScreen

@Composable
fun AppNavHost(
    backStack: MutableList<AppNavKey>,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() }
    ) { key ->
        when (key) {
            AppNavKey.RecentlyShortenUrls ->
                NavEntry(key) {
                    RecentlyShortenUrlsScreen(
                        onBack = {
                            backStack.clear()
                            backStack.add(AppNavKey.Home)
                        }
                    )
                }

            AppNavKey.Home ->
                NavEntry(key) {
                    HomeScreen(
                        onRecentlyShortenUrls = {
                            backStack.add(AppNavKey.RecentlyShortenUrls)
                        }
                    )
                }
        }
    }
}