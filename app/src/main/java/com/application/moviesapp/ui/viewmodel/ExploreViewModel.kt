package com.application.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.application.moviesapp.UserPreferences
import com.application.moviesapp.data.SORT_BY
import com.application.moviesapp.data.api.response.MovieSimpleResponse
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.MoviesDiscoverUseCase
import com.application.moviesapp.domain.MoviesSortUseCase
import com.application.moviesapp.domain.MoviesWithSort
import com.application.moviesapp.domain.TvSeriesDiscoverUseCase
import com.application.moviesapp.domain.model.MovieGenre
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.domain.usecase.AccountSetupUseCase
import com.application.moviesapp.domain.usecase.MovieGenresUseCase
import com.application.moviesapp.domain.usecase.MovieSearchUseCase
import com.application.moviesapp.domain.usecase.TvSeriesGenreUseCase
import com.application.moviesapp.ui.home.Categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
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

data class SearchUiState(val search: String = "", val clicked: Boolean = false)

data class SortAndFilterUiState(val genre: String = "",
                                val sortBy: String = SORT_BY.POPULARITY.title,
                                val includeAdult: Boolean = false)

@HiltViewModel
class ExploreViewModel @Inject constructor(private val useCase: MoviesSortUseCase,
                                           private val repository: MoviesRepository,
                                           private val moviesDiscoverUseCase: MoviesDiscoverUseCase,
                                           private val tvSeriesDiscoverUseCase: TvSeriesDiscoverUseCase,
                                           private val movieGenresUseCase: MovieGenresUseCase,
                                           private val tvSeriesGenreUseCase: TvSeriesGenreUseCase,
                                           private val movieSearchUseCase: MovieSearchUseCase,
                                           private val accountSetupUseCase: AccountSetupUseCase): ViewModel() {

    private companion object {
        const val TAG = "ExploreViewModel"
    }

    val readUserPreference = accountSetupUseCase.readUserPreference.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = UserPreferences.getDefaultInstance())


    private var _exploreUiState = MutableStateFlow<ExploreUiState>(ExploreUiState.Loading)
    val exploreUiState: StateFlow<ExploreUiState> get() = _exploreUiState

    private var _genreUiState = MutableStateFlow<Resource<MovieGenre>>(Resource.Loading)
    val genreUiState get() = _genreUiState.asStateFlow()

    private var _searchInputUiState = MutableStateFlow(SearchUiState())
    val searchInputUiState get() = _searchInputUiState.asStateFlow()

    private var _sortAndFilterUiState = MutableStateFlow(SortAndFilterUiState())
    val sortAndFilterUiState get() = _sortAndFilterUiState.asStateFlow()


    fun moviesPagingFlow(genres: String = "",
                         sortBy: String = SORT_BY.POPULARITY.title,
                         includeAdult: Boolean = false) =
        moviesDiscoverUseCase(
            genre = genres,
            sortBy = sortBy,
            includeAdult = includeAdult).cachedIn(viewModelScope)

    fun tvSeriesPagingFlow(genres: String = "",
                         sortBy: String = SORT_BY.POPULARITY.title,
                         includeAdult: Boolean = false) =
        tvSeriesDiscoverUseCase(
            genre = "",
            sortBy = sortBy,
            includeAdult = includeAdult).cachedIn(viewModelScope)

    fun getMovieBySearch(search: String = "") = movieSearchUseCase(search).cachedIn(viewModelScope)

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

    fun updateGenre(genre: MovieGenre.Genre) = viewModelScope.launch {
        val genreList = mutableListOf<MovieGenre.Genre>().apply {
            addAll(readUserPreference.value.genreList.map { MovieGenre.Genre(it.id, it.name) })
        }

        if (genreList.contains(genre)) {
            genreList.remove(genre)
            accountSetupUseCase.updateGenre(genre = genreList.map { MoviesDetail.Genre(it.id, it.name) }.toSet())

            Timber.tag(TAG).d(genreList.toString())
        } else {
            genreList.add(genre)
            accountSetupUseCase.updateGenre(genre = genreList.map { MoviesDetail.Genre(it.id, it.name) }.toSet())
            Timber.tag(TAG).d(genreList.toString())
        }
    }

    fun setSortAndFilter(genre: List<MovieGenre.Genre> = emptyList(),
                         sortBy: SORT_BY = SORT_BY.POPULARITY,
                         includeAdult: Boolean = false) {
        _sortAndFilterUiState.update {
            it.copy(
                genre = genre.map { genre ->  genre.id }.joinToString(" | "),
                sortBy = sortBy.title,
                includeAdult = includeAdult
            )
        }
    }
}