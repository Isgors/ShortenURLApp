package dev.igordesouza.orthos.devtools

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * DevTools persistent override for Orthos enablement.
 *
 * Values:
 * 0 = DEFAULT (no override)
 * 1 = FORCE_ON
 * 2 = FORCE_OFF
 *
 * Exposed as reflection-friendly APIs so orthos-sdk does NOT depend on devtools.
 */
object OrthosDevTools {

    private val Context.dataStore by preferencesDataStore(name = "orthos_devtools")

    private val KEY_FORCE_MODE: Preferences.Key<Int> =
        intPreferencesKey("orthos_force_mode")

    const val MODE_DEFAULT = 0
    const val MODE_FORCE_ON = 1
    const val MODE_FORCE_OFF = 2

    /** Non-suspending getter, safe for Gate usage during SDK install. */
    @JvmStatic
    fun getForceModeBlocking(context: Context): Int = runBlocking {
        context.applicationContext.dataStore.data
            .map { prefs -> prefs[KEY_FORCE_MODE] ?: MODE_DEFAULT }
            .first()
    }

    /** Suspending setter used by the Dev Panel. */
    suspend fun setForceMode(context: Context, mode: Int) {
        context.applicationContext.dataStore.edit { prefs ->
            prefs[KEY_FORCE_MODE] = mode
        }
    }

    /** Flow useful for UI. */
    fun forceModeFlow(context: Context): Flow<Int> =
        context.applicationContext.dataStore.data
            .map { prefs -> prefs[KEY_FORCE_MODE] ?: MODE_DEFAULT }
}
