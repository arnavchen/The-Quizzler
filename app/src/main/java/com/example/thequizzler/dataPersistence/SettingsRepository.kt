package com.example.thequizzler.dataPersistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// At the top level of your file, outside the class, declare the DataStore instance.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Repository for managing user preferences using Jetpack DataStore.
 */
class SettingsRepository(context: Context) {

    // A reference to the application's datastore.
    private val dataStore = context.dataStore

    // Define the keys for your preferences based on the SettingsScreen UI.
    // This makes them type-safe and prevents typos.
    private object PreferencesKeys {
        val LOCATION_ENABLED = booleanPreferencesKey("location_enabled")
        val OFFLINE_MODE = booleanPreferencesKey("offline_mode")
        val MEASUREMENT_SYSTEM = stringPreferencesKey("measurement_system")
    }

    // --- Location Questions Setting ---

    /**
     * A flow that emits the current state of the location questions setting.
     * It will default to 'true' if the preference has not been set yet.
     */
    val isLocationEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.LOCATION_ENABLED] ?: true
    }

    /**
     * Suspended function to update the location questions setting.
     * @param isEnabled The new value for the setting.
     */
    suspend fun setLocationEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LOCATION_ENABLED] = isEnabled
        }
    }

    // --- Offline Mode Setting ---

    /**
     * A flow that emits the current state of the offline mode setting.
     * It will default to 'false' if the preference has not been set yet.
     */
    val isOfflineMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.OFFLINE_MODE] ?: false
    }

    /**
     * Suspended function to update the offline mode setting.
     * @param isEnabled The new value for the setting.
     */
    suspend fun setOfflineMode(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.OFFLINE_MODE] = isEnabled
        }
    }

    // --- Measurement System Setting ---

    /**
     * A flow that emits the current selected measurement system.
     * It will default to 'Imp' if the preference has not been set yet.
     */
    val measurementSystem: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.MEASUREMENT_SYSTEM] ?: "Imp"
    }

    /**
     * Suspended function to update the measurement system setting.
     * @param system The new value for the setting ("Imp" or "Met").
     */
    suspend fun setMeasurementSystem(system: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MEASUREMENT_SYSTEM] = system
        }
    }
}
