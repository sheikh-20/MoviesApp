package com.application.moviesapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.application.moviesapp.domain.model.AppUpdatesPreference
import com.application.moviesapp.domain.model.GeneralNotificationPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

interface NotificationPreferenceRepository {
    val readGeneralNotificationPreference: Flow<GeneralNotificationPreference>

    val readAppUpdatesPreference: Flow<AppUpdatesPreference>

    suspend fun updateGeneralNotificationPreference(value: Boolean)
    suspend fun updateAppUpdatesPreference(value: Boolean)
}

class NotificationPreferencePreferenceImpl @Inject constructor(private val datastore: DataStore<Preferences>): NotificationPreferenceRepository {
    private object PreferenceKeys {
        val IS_GENERAL_NOTIFICATION = booleanPreferencesKey("is_general_notification")
        val IS_APP_UPDATES = booleanPreferencesKey("is_app_updates")
    }

    override val readGeneralNotificationPreference: Flow<GeneralNotificationPreference>
        get() = datastore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {  preference ->
                val isGeneralNotification = preference[PreferenceKeys.IS_GENERAL_NOTIFICATION] ?: false
                GeneralNotificationPreference(isGeneralNotification)
            }
    override val readAppUpdatesPreference: Flow<AppUpdatesPreference>
        get() = datastore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {  preference ->
                val isAppUpdates = preference[PreferenceKeys.IS_APP_UPDATES] ?: false
                AppUpdatesPreference(isAppUpdates)
            }
    override suspend fun updateGeneralNotificationPreference(value: Boolean) {
        datastore.edit { preference ->
            preference[PreferenceKeys.IS_GENERAL_NOTIFICATION] = value
        }
    }

    override suspend fun updateAppUpdatesPreference(value: Boolean) {
        datastore.edit { preference ->
            preference[PreferenceKeys.IS_APP_UPDATES] = value
        }
    }

}