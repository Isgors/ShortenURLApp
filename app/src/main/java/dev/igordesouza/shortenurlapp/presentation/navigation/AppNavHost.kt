package dev.igordesouza.shortenurlapp.presentation.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import dev.igordesouza.shortenurlapp.presentation.home.HomeScreen
import dev.igordesouza.shortenurlapp.presentation.security.OrthosGateScreen
import dev.igordesouza.shortenurlapp.presentation.security.OrthosVerdictScreen

@Composable
fun AppNavHost(
    backStack: MutableList<AppNavKey>,
    activity: Activity,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = {
            backStack.removeLastOrNull()

//            // Back manual e controlado
//            if (backStack.lastOrNull() is AppNavKey.Home) {
//                // Não volta para Orthos
//                activity.finishAffinity()
//            } else {
//                backStack.removeLastOrNull()
//            }
        }
    ) { key ->
        when (key) {
            AppNavKey.Gate ->
                NavEntry(key) {
                    OrthosGateScreen(
                        onGoHome = {
                            backStack.clear()
                            backStack.add(AppNavKey.Home)
                        },
                        onShowVerdict = {
                            backStack.clear()
                            backStack.add(AppNavKey.OrthosVerdict)
                        }
                    )
                }

            AppNavKey.OrthosVerdict ->
                NavEntry(key) {
                    OrthosVerdictScreen(
                        onContinue = {
                            // 🔥 Replace total do stack
                            backStack.clear()
                            backStack.add(AppNavKey.Home)
                        },
                        onExit = {
                            activity.finishAffinity()
                        }
                    )
                }

            AppNavKey.Home ->
                NavEntry(key) {
                    HomeScreen()
                }
        }
    }
}
