package dev.igordesouza.shortenurlapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppNavKey {

    @Serializable
    data object Home : AppNavKey

    @Serializable
    data object RecentlyShortenUrls : AppNavKey
}