package com.application.moviesapp.ui.utility

import com.application.moviesapp.BuildConfig

sealed interface UiState {
    object Loading: UiState
    data class Success<out T>(val movies: T): UiState
    object Failure: UiState
}

val String.toImageUrl: String
    get() {
        return BuildConfig.IMAGE_BASE_URL + this
    }
