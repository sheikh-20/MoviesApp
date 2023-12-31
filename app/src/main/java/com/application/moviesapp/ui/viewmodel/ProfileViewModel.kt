package com.application.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.domain.model.LanguagePreference
import com.application.moviesapp.domain.model.SettingsPreference
import com.application.moviesapp.domain.usecase.LanguageUseCase
import com.application.moviesapp.domain.usecase.SettingsUseCase
import com.application.moviesapp.ui.language.language
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val settingsUseCase: SettingsUseCase, private val languageUseCase: LanguageUseCase): ViewModel() {

    val isDarkMode: Flow<SettingsPreference> = settingsUseCase.readFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SettingsPreference(false)
    )

    fun updateMode(value: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        settingsUseCase.updatePreference(value)
    }


    val selectLanguage: Flow<LanguagePreference> = languageUseCase.readFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LanguagePreference(language = language[0].language[0])
    )

    fun updateLanguage(value: String) = viewModelScope.launch(Dispatchers.IO) {
        languageUseCase.updatePreference(value)
    }
}