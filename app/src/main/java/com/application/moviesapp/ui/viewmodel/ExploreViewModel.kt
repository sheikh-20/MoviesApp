package com.application.moviesapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.api.response.MovieSimpleResponse
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

sealed interface ExploreUiState {
    object Loading: ExploreUiState
    data class Success(val response: MovieSimpleResponse): ExploreUiState
    object Failure: ExploreUiState
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


    private var _exploreUiState = MutableStateFlow<ExploreUiState>(ExploreUiState.Loading)
    val exploreUiState: StateFlow<ExploreUiState> get() = _exploreUiState


    var searchInputField by mutableStateOf("")
        private set

    fun getTrendingMovies() = viewModelScope.launch(Dispatchers.IO) {
        _exploreUiState.value = ExploreUiState.Loading

        try {
            val result = repository.getMovieTrending()
            _exploreUiState.value = ExploreUiState.Success(result)
            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _exploreUiState.value = ExploreUiState.Failure
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

    fun updateSearchField(value: String) {
        searchInputField = value

        if (searchInputField.isNotEmpty()) {
            getSearchResults()
        }
    }

    fun getSearchResults() = viewModelScope.launch(Dispatchers.IO) {
        _exploreUiState.value = ExploreUiState.Loading

        try {
            val result = repository.getSearchResults(searchInputField)

            if (result.results?.isEmpty() == true) {
                throw IOException()
            }

            _exploreUiState.value = ExploreUiState.Success(result)

            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _exploreUiState.value = ExploreUiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    init {
        getMovieSortFilter()
    }
}