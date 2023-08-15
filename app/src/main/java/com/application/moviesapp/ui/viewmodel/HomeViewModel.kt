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

sealed interface HomeUiState {
    object Loading: HomeUiState
    data class Success<out T>(val movies: T): HomeUiState
    object Failure: HomeUiState
}
@HiltViewModel
class HomeViewModel @Inject constructor(private val moviesRepository: MoviesRepository): ViewModel() {

    private companion object {
        const val TAG = "HomeViewModel"
    }

    private var _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    private var _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    fun getPopularMoviesList() = viewModelScope.launch(Dispatchers.IO) {
        _homeUiState.value = HomeUiState.Loading
        try {
            val result = moviesRepository.getPopularMoviesList()
            _homeUiState.value = HomeUiState.Success(result)
            Timber.tag(TAG).d(result)
        } catch (exception: IOException) {
            _homeUiState.value = HomeUiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    fun getMoviesGenreList() = viewModelScope.launch(Dispatchers.IO) {
        _homeUiState.value = HomeUiState.Loading

        try {
            val result = moviesRepository.getMoviesGenreList()
            _homeUiState.value = HomeUiState.Success<MovieGenreResponse>(result)
            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _homeUiState.value = HomeUiState.Failure
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