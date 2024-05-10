package com.application.moviesapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.application.moviesapp.domain.model.SettingsPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class WifiPreferenceImpl @Inject constructor(private val datastore: DataStore<Preferences>): SettingsPreferenceRepository {
    private object PreferenceKeys {
        val IS_WIFI = booleanPreferencesKey("is_wifi")
    }

    override val readPreference: Flow<SettingsPreference>
        get() = datastore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {  preference ->
                val isWifiEnabled = preference[PreferenceKeys.IS_WIFI] ?: false
                SettingsPreference(isWifiEnabled)
            }

    override suspend fun updatePreference(value: Boolean) {
        datastore.edit { preference ->
            preference[PreferenceKeys.IS_WIFI] = value
        }
    }
}