package com.application.moviesapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.application.moviesapp.data.api.response.MovieSimpleResponse
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.local.entity.MoviesEntity
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.MoviesPopularUseCase
import com.application.moviesapp.domain.MoviesSortUseCase
import com.application.moviesapp.domain.MoviesWithSort
import com.application.moviesapp.domain.model.MovieGenre
import com.application.moviesapp.domain.usecase.MovieGenresUseCase
import com.application.moviesapp.domain.usecase.MovieSearchUseCase
import com.application.moviesapp.domain.usecase.TvSeriesGenreUseCase
import com.application.moviesapp.ui.home.Categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

data class SearchUiState(val search: String = "", val clicked: Boolean = false)

@HiltViewModel
class ExploreViewModel @Inject constructor(private val useCase: MoviesSortUseCase,
                                           private val repository: MoviesRepository,
    private val moviesPopularUseCase: MoviesPopularUseCase,
    private val movieGenresUseCase: MovieGenresUseCase,
    private val tvSeriesGenreUseCase: TvSeriesGenreUseCase,
    private val movieSearchUseCase: MovieSearchUseCase): ViewModel() {

    private companion object {
        const val TAG = "ExploreViewModel"
    }


    private var _movieSortUiState = MutableStateFlow<MovieSortUiState>(MovieSortUiState.Loading)
    val movieSortUiState: StateFlow<MovieSortUiState> get() = _movieSortUiState


    private var _exploreUiState = MutableStateFlow<ExploreUiState>(ExploreUiState.Loading)
    val exploreUiState: StateFlow<ExploreUiState> get() = _exploreUiState

    private var _genreUiState = MutableStateFlow<Resource<MovieGenre>>(Resource.Loading)
    val genreUiState get() = _genreUiState.asStateFlow()

    private var _searchInputUiState = MutableStateFlow(SearchUiState())
    val searchInputUiState get() = _searchInputUiState.asStateFlow()


    val moviesPagingFlow = moviesPopularUseCase().cachedIn(viewModelScope)
    fun getMovieBySearch(search: String = "") = movieSearchUseCase(search).cachedIn(viewModelScope)

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

    fun updateClickInput(clicked: Boolean) {
        _searchInputUiState.update {
            it.copy(clicked = clicked)
        }
    }

    fun updateSearchField(value: String) {
        _searchInputUiState.update {
            it.copy(search = value)
        }
    }

    fun getGenre(categories: Categories) = viewModelScope.launch {
        when (categories) {
            Categories.Movies -> {
                _genreUiState.value = movieGenresUseCase()
            }
            Categories.TV -> {
                _genreUiState.value = tvSeriesGenreUseCase()
            }
        }
    }

    init {
        getMovieSortFilter()
    }
}