package dev.igordesouza.shortenurlapp.presentation.security

import androidx.lifecycle.ViewModel
import dev.igordesouza.orthos.sdk.Orthos

class OrthosGateViewModel(
    private val orthos: Orthos
) : ViewModel() {

    /**
     * Single source of truth for the UI:
     * - true  -> show Orthos verdict screen
     * - false -> skip security and go straight to Home
     */
    fun isOrthosEnabled(): Boolean = orthos.isEnabled()
}
