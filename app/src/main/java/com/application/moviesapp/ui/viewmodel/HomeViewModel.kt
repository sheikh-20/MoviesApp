package com.application.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.api.response.MovieNewReleasesResponse
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.ui.utility.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val moviesRepository: MoviesRepository): ViewModel() {

    private companion object {
        const val TAG = "HomeViewModel"
    }

    private var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun getMovieNewReleases() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = UiState.Loading

        try {
            val result = moviesRepository.getNewReleasesList()
            _uiState.value = UiState.Success<MovieNewReleasesResponse>(result)
            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _uiState.value = UiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    init {
        getMovieNewReleases()
    }
}