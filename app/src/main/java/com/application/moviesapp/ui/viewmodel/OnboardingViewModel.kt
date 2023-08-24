package com.application.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

sealed interface MovieGenreUiState {
    object Loading: MovieGenreUiState
    data class Success(val genreResponse: MovieGenreResponse): MovieGenreUiState
    object Failure: MovieGenreUiState
}
@HiltViewModel
class OnboardingViewModel @Inject constructor(private val moviesRepository: MoviesRepository): ViewModel() {

    private companion object {
        const val TAG = "OnboardingViewModel"
    }

    private var _movieGenreUiState = MutableStateFlow<MovieGenreUiState>(MovieGenreUiState.Loading)
    val movieGenreUiState: StateFlow<MovieGenreUiState> = _movieGenreUiState

    private var _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    fun getMoviesGenreList() = viewModelScope.launch(Dispatchers.IO) {
        _movieGenreUiState.value = MovieGenreUiState.Loading

        try {
            val result = moviesRepository.getMoviesGenreList()
            _movieGenreUiState.value = MovieGenreUiState.Success(result)
            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _movieGenreUiState.value = MovieGenreUiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    private fun showLoading() = viewModelScope.launch {
        delay(3_000L)
        _loading.value = false
    }

    init {
        showLoading()
    }
}