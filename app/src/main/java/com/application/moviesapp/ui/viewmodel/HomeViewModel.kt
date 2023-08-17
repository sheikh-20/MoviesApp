package com.application.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.api.response.MovieNewReleasesResponse
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.MoviesUseCase
import com.application.moviesapp.domain.MoviesWithNewReleases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

sealed interface MoviesWithNewReleaseUiState {
    object Loading: MoviesWithNewReleaseUiState
    data class Success(val moviesWithNewReleases: MoviesWithNewReleases): MoviesWithNewReleaseUiState
    object Failure: MoviesWithNewReleaseUiState
}
sealed interface MovieNewReleaseUiState {
    object Loading: MovieNewReleaseUiState
    data class Success(val moviesNewReleases: MovieNewReleasesResponse): MovieNewReleaseUiState
    object Failure: MovieNewReleaseUiState
}
sealed interface MovieTopRatedUiState {
    object Loading: MovieTopRatedUiState
    data class Success(val movieTopRated: MovieTopRatedResponse): MovieTopRatedUiState
    object Failure: MovieTopRatedUiState
}
@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: MoviesUseCase, private val moviesRepository: MoviesRepository): ViewModel() {

    private companion object {
        const val TAG = "HomeViewModel"
    }

    private var _moviesWithNewReleaseUiState = MutableStateFlow<MoviesWithNewReleaseUiState>(MoviesWithNewReleaseUiState.Loading)
    val moviesWithNewReleaseUiState: StateFlow<MoviesWithNewReleaseUiState> = _moviesWithNewReleaseUiState

    private var _moviesNewReleaseUiState = MutableStateFlow<MovieNewReleaseUiState>(MovieNewReleaseUiState.Loading)
    val moviesNewReleaseUiState: StateFlow<MovieNewReleaseUiState> = _moviesNewReleaseUiState

    private var _moviesTopRatedUiState = MutableStateFlow<MovieTopRatedUiState>(MovieTopRatedUiState.Loading)
    val movieTopRatedUiState: StateFlow<MovieTopRatedUiState> = _moviesTopRatedUiState

    fun getMoviesWithNewReleases() = viewModelScope.launch(Dispatchers.IO) {
        _moviesWithNewReleaseUiState.value = MoviesWithNewReleaseUiState.Loading

        try {
            val result = useCase.invoke()
            _moviesWithNewReleaseUiState.value = MoviesWithNewReleaseUiState.Success(result)
            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _moviesWithNewReleaseUiState.value = MoviesWithNewReleaseUiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    fun getMovieNewReleases() = viewModelScope.launch(Dispatchers.IO) {
        _moviesNewReleaseUiState.value = MovieNewReleaseUiState.Loading

        try {
            val result = moviesRepository.getNewReleasesList()
            _moviesNewReleaseUiState.value = MovieNewReleaseUiState.Success(result)
            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _moviesNewReleaseUiState.value = MovieNewReleaseUiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    fun getMoviesTopRated() = viewModelScope.launch {
        _moviesTopRatedUiState.value = MovieTopRatedUiState.Loading

        try {
            val result = moviesRepository.getMoviesTopRated()
            _moviesTopRatedUiState.value = MovieTopRatedUiState.Success(result)
            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _moviesTopRatedUiState.value = MovieTopRatedUiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }
}