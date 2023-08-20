package com.application.moviesapp.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import com.application.moviesapp.data.api.response.MovieTrendingResponse
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.MoviesSortUseCase
import com.application.moviesapp.domain.MoviesWithSort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

sealed interface MovieTrendingUiState {
    object Loading: MovieTrendingUiState
    data class Success(val movieTrendingResponse: MovieTrendingResponse): MovieTrendingUiState
    object Failure: MovieTrendingUiState
}

sealed interface MovieSortUiState {
    object Loading: MovieSortUiState
    data class Success(val moviesWithSort: MoviesWithSort): MovieSortUiState
    object Failure: MovieSortUiState
}
@HiltViewModel
class ExploreViewModel @Inject constructor(private val useCase: MoviesSortUseCase, private val repository: MoviesRepository): ViewModel() {


    private companion object {
        const val TAG = "ExploreViewModel"
    }


    private var _movieSortUiState = MutableStateFlow<MovieSortUiState>(MovieSortUiState.Loading)
    val movieSortUiState: StateFlow<MovieSortUiState> get() = _movieSortUiState


    private var _movieTrendingUiState = MutableStateFlow<MovieTrendingUiState>(MovieTrendingUiState.Loading)
    val movieTrendingUiState: StateFlow<MovieTrendingUiState> get() = _movieTrendingUiState

    fun getTrendingMovies() = viewModelScope.launch(Dispatchers.IO) {
        _movieTrendingUiState.value = MovieTrendingUiState.Loading

        try {
            val result = repository.getMovieTrending()
            _movieTrendingUiState.value = MovieTrendingUiState.Success(result)
//            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _movieTrendingUiState.value = MovieTrendingUiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    fun getMovieSortFilter() = viewModelScope.launch(Dispatchers.IO) {
        _movieSortUiState.value = MovieSortUiState.Loading

        try {
            val result = useCase.invoke()
            _movieSortUiState.value = MovieSortUiState.Success(result)
            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _movieSortUiState.value = MovieSortUiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    init {
        getMovieSortFilter()
    }
}