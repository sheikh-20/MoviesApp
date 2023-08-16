package com.application.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.ui.utility.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val moviesRepository: MoviesRepository): ViewModel() {

    private companion object {
        const val TAG = "OnboardingViewModel"
    }

    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private var _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    fun getPopularMoviesList() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = UiState.Loading
        try {
            val result = moviesRepository.getPopularMoviesList()
            _uiState.value = UiState.Success(result)
            Timber.tag(TAG).d(result)
        } catch (exception: IOException) {
            _uiState.value = UiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    fun getMoviesGenreList() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = UiState.Loading

        try {
            val result = moviesRepository.getMoviesGenreList()
            _uiState.value = UiState.Success<MovieGenreResponse>(result)
            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _uiState.value = UiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    private fun showLoading() = viewModelScope.launch {
        delay(3_000L)
        _loading.value = false
    }

    init {
        showLoading()
//        getPopularMoviesList()
    }
}