package dev.igordesouza.orthos.devtools

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OrthosDevPanelViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {

    private val appContext = context.applicationContext

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrthosDevPanelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrthosDevPanelViewModel(appContext) as T
        }
        error("Unknown ViewModel: $modelClass")
    }
}
