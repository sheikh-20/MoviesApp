package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.repository.SettingsPreferenceRepository
import com.application.moviesapp.domain.model.SettingsPreference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WifiUseCase {
    val readFlow: Flow<SettingsPreference>
    suspend fun updatePreference(value: Boolean)
}

class GetWifiInteractor @Inject constructor(private val repository: SettingsPreferenceRepository): WifiUseCase {
    override val readFlow: Flow<SettingsPreference>
        get() = repository.readPreference

    override suspend fun updatePreference(value: Boolean) = repository.updatePreference(value)
}