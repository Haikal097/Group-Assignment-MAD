package com.example.groupassignment.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DS_NAME = "settings_datastore"

// Context extension (singleton datastore)
private val Context.dataStore by preferencesDataStore(name = DS_NAME)

class SettingsDataStore(private val context: Context) {

    companion object {
        private val KEY_DARK_MODE: Preferences.Key<Boolean> = booleanPreferencesKey("dark_mode")
    }

    val darkModeFlow: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_DARK_MODE] ?: false
        }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DARK_MODE] = enabled
        }
    }
}
