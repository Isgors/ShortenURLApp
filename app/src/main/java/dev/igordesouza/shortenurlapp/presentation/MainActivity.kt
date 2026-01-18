package dev.igordesouza.shortenurlapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import dev.igordesouza.shortenurlapp.presentation.navigation.AppNavHost
import dev.igordesouza.shortenurlapp.presentation.navigation.AppNavKey
import dev.igordesouza.shortenurlapp.presentation.theme.ShortenURLAppTheme

class MainActivity : ComponentActivity() {

    val backStack: MutableList<AppNavKey> =
        mutableStateListOf(AppNavKey.Gate)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShortenURLAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(
                        backStack = backStack,
                        activity = this
                    )
                }
            }
        }
    }
}
