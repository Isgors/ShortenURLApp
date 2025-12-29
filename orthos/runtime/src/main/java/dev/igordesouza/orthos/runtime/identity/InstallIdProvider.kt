package dev.igordesouza.orthos.runtime.identity

import android.content.Context
import java.util.UUID
import androidx.core.content.edit

/**
 * Responsible for providing a stable install-scoped identifier.
 *
 * This ID must survive app restarts and updates,
 * but can be reset on reinstall.
 */
class InstallIdProvider(
    private val context: Context
) {

    fun get(): String {
        val prefs = context.getSharedPreferences(
            "orthos_install",
            Context.MODE_PRIVATE
        )

        val existing = prefs.getString("install_id", null)
        if (existing != null) return existing

        val newId = UUID.randomUUID().toString()
        prefs.edit { putString("install_id", newId) }
        return newId
    }
}
