package dev.igordesouza.shortenurlapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppNavKey {

    /**
     * Security gate that decides the real app entry point.
     *
     * - If Orthos is disabled for this build variant, we skip directly to [Home].
     * - If enabled, we show [OrthosVerdict].
     */
    @Serializable
    data object Gate : AppNavKey

    @Serializable
    data object OrthosVerdict : AppNavKey

    @Serializable
    data object Home : AppNavKey
}
