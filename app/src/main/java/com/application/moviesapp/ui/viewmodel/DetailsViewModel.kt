package com.application.moviesapp.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.api.response.MovieDetailsDto
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.domain.usecase.MovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val useCase: MovieDetailsUseCase, private val repository: MoviesRepository): ViewModel() {

    private companion object {
        const val TAG = "DetailsViewModel"
    }

    private var _movieDetailResponse = MutableStateFlow<Resource<MoviesDetail>>(Resource.Loading)
    val movieDetailResponse  get() = _movieDetailResponse.asStateFlow()

    fun getMovieDetail(movieId: Int) = viewModelScope.launch {
        _movieDetailResponse.value = useCase(movieId)
        Timber.tag(TAG).d(movieDetailResponse.value.toString())
    }
}