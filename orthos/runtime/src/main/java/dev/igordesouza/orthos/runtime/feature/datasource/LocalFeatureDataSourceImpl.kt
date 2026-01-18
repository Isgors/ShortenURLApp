package dev.igordesouza.orthos.runtime.feature.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.igordesouza.orthos.runtime.feature.FeatureSnapshot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore("orthos_features")

class LocalFeatureDataSourceImpl(
    private val context: Context,
    private val json: Json
) : LocalFeatureDataSource {

    private val key = stringPreferencesKey("snapshot")

    override fun get(): FeatureSnapshot? = runBlocking {
        val prefs = context.dataStore.data.first()
        prefs[key]?.let { json.decodeFromString(it) }
    }

    override fun save(snapshot: FeatureSnapshot): Unit = runBlocking {
        context.dataStore.edit { prefs ->
            prefs[key] = json.encodeToString(snapshot)
        }
    }

    override fun clear(): Unit = runBlocking {
        context.dataStore.edit { prefs ->
            prefs.remove(key)
        }
    }
}

