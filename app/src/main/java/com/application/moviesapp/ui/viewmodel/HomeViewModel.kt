package com.application.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.application.moviesapp.data.api.response.MovieTopRatedResponse
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.remote.MovieNewReleasesDto
import com.application.moviesapp.domain.MoviesNowPlayingUseCase
import com.application.moviesapp.domain.model.MovieWithTvSeries
import com.application.moviesapp.domain.usecase.MovieWithTvSeriesUseCase
import com.application.moviesapp.domain.usecase.TvSeriesNowPlayingUseCase
import com.application.moviesapp.ui.signin.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MovieNewReleaseUiState {
    object Loading: MovieNewReleaseUiState
    data class Success(val moviesNewReleases: MovieNewReleasesDto): MovieNewReleaseUiState
    object Failure: MovieNewReleaseUiState
}
sealed interface MovieTopRatedUiState {
    object Loading: MovieTopRatedUiState
    data class Success(val movieTopRated: MovieTopRatedResponse): MovieTopRatedUiState
    object Failure: MovieTopRatedUiState
}
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesNowPlayingUseCase: MoviesNowPlayingUseCase,
    private val tvSeriesNowPlayingUseCase: TvSeriesNowPlayingUseCase,
    private val movieWithTvSeriesUseCase: MovieWithTvSeriesUseCase
    ): ViewModel() {

    private companion object {
        const val TAG = "HomeViewModel"
    }

    private var _movieWithTvSeriesUiState = MutableStateFlow<Resource<MovieWithTvSeries>>(Resource.Loading)
    val movieWithTvSeriesUiState: StateFlow<Resource<MovieWithTvSeries>> = _movieWithTvSeriesUiState

    private var _moviesNewReleaseUiState = MutableStateFlow<MovieNewReleaseUiState>(MovieNewReleaseUiState.Loading)
    val moviesNewReleaseUiState: StateFlow<MovieNewReleaseUiState> = _moviesNewReleaseUiState

    private var _moviesTopRatedUiState = MutableStateFlow<MovieTopRatedUiState>(MovieTopRatedUiState.Loading)
    val movieTopRatedUiState: StateFlow<MovieTopRatedUiState> = _moviesTopRatedUiState

    private val auth = Firebase.auth

    private var _profileInfoUiState = MutableStateFlow<UserData>(UserData(userId = "", userName = "", profilePictureUrl = "", email = ""))
    val profileInfoUiState: StateFlow<UserData> = _profileInfoUiState

    fun getMovieWithTvSeries() = viewModelScope.launch {
        _movieWithTvSeriesUiState.value = movieWithTvSeriesUseCase()
    }
 
//    fun getMovieNewReleases() = viewModelScope.launch(Dispatchers.IO) {
//        _moviesNewReleaseUiState.value = MovieNewReleaseUiState.Loading
//
//        try {
//            val result = moviesRepository.getNewReleasesList()
//            _moviesNewReleaseUiState.value = MovieNewReleaseUiState.Success(result)
//            Timber.tag(TAG).d(result.toString())
//        } catch (exception: IOException) {
//            _moviesNewReleaseUiState.value = MovieNewReleaseUiState.Failure
//            Timber.tag(TAG).e(exception)
//        }
//    }
//
//    fun getMoviesTopRated() = viewModelScope.launch {
//        _moviesTopRatedUiState.value = MovieTopRatedUiState.Loading
//
//        try {
//            val result = moviesRepository.getMoviesTopRated()
//            _moviesTopRatedUiState.value = MovieTopRatedUiState.Success(result)
//            Timber.tag(TAG).d(result.toString())
//        } catch (exception: IOException) {
//            _moviesTopRatedUiState.value = MovieTopRatedUiState.Failure
//            Timber.tag(TAG).e(exception)
//        }
//    }

    fun nowPlayingMoviesPagingFlow() = moviesNowPlayingUseCase().cachedIn(viewModelScope)

    fun nowPlayingSeriesPagingFlow() = tvSeriesNowPlayingUseCase().cachedIn(viewModelScope)


    private fun getSignedInUser() {
        auth.currentUser?.apply {
            _profileInfoUiState.value = UserData(
                userId = uid,
                userName = displayName,
                profilePictureUrl = photoUrl.toString(),
                email = email
            )
        }
    }

    fun signOut() = viewModelScope.launch {
        auth.signOut()
    }


    init {
        getSignedInUser()
    }

}