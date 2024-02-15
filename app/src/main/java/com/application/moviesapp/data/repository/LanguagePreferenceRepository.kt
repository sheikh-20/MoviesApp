package com.application.moviesapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.application.moviesapp.domain.model.LanguagePreference
import com.application.moviesapp.domain.model.SettingsPreference
import com.application.moviesapp.ui.language.language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

interface LanguagePreferenceRepository {
    val readPreference: Flow<LanguagePreference>
    suspend fun updatePreference(value: String)
}

class LanguagePreferenceImpl @Inject constructor(private val datastore: DataStore<Preferences>): LanguagePreferenceRepository {

    private object PreferenceKeys {
        val LANGUAGE = stringPreferencesKey("language")
    }

    override val readPreference: Flow<LanguagePreference>
        get() = datastore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map {  preference ->
                val language = preference[PreferenceKeys.LANGUAGE] ?: language[0].language[0]
                LanguagePreference(language)
            }


    override suspend fun updatePreference(value: String) {
        datastore.edit { preference ->
            preference[PreferenceKeys.LANGUAGE] = value
        }
    }

}