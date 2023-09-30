package com.application.moviesapp.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.api.response.MovieDetailsDto
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieState
import com.application.moviesapp.domain.model.MovieTrailerWithYoutube
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.domain.usecase.GetMovieStateInteractor
import com.application.moviesapp.domain.usecase.MovieDetailsUseCase
import com.application.moviesapp.domain.usecase.MovieStateUseCase
import com.application.moviesapp.domain.usecase.MovieTrailerUseCase
import com.application.moviesapp.domain.usecase.MovieUpdateFavouriteInteractor
import com.application.moviesapp.domain.usecase.YoutubeThumbnailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val useCase: MovieDetailsUseCase,
                                           private val trailerUseCase: MovieTrailerUseCase,
                                           private val updateMovieFavouriteUseCase: MovieUpdateFavouriteInteractor,
    private val getMovieStateUseCase: MovieStateUseCase
): ViewModel() {

    private companion object {
        const val TAG = "DetailsViewModel"
    }

    private var _movieDetailResponse = MutableStateFlow<Resource<MoviesDetail>>(Resource.Loading)
    val movieDetailResponse  get() = _movieDetailResponse.asStateFlow()


    private var _movieTrailerResponse = MutableStateFlow<Resource<List<MovieTrailerWithYoutube>>>(Resource.Loading)
    val movieTrailerResponse get() = _movieTrailerResponse.asStateFlow()


    private var _movieStateResponse = MutableStateFlow<Resource<MovieState>>(Resource.Loading)
    val movieStateResponse get() = _movieStateResponse.asStateFlow()

    fun getMovieDetail(movieId: Int) = viewModelScope.launch {
        _movieDetailResponse.value = useCase(movieId)
        Timber.tag(TAG).d(movieDetailResponse.value.toString())
    }

    fun getMovieTrailer(movieId: Int) = viewModelScope.launch {
        _movieTrailerResponse.value = trailerUseCase(movieId)
        Timber.tag(TAG).d(movieTrailerResponse.value.toString())
    }

    fun updateMovieFavourite(mediaType: String, mediaId: Int, isFavorite: Boolean) = viewModelScope.launch {
        val jsonData = JSONObject()
        try {
            jsonData.put("media_type", mediaType)
            jsonData.put("media_id", mediaId)
            jsonData.put("favorite", isFavorite)
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonData.toString().toRequestBody(mediaType)

        updateMovieFavouriteUseCase(requestBody)
    }

    fun getMovieState(movieId: Int) = viewModelScope.launch {
        _movieStateResponse.value = getMovieStateUseCase(movieId)
    }
}