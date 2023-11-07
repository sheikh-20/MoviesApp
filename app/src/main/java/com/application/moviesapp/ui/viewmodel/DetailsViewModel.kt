package com.application.moviesapp.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.application.moviesapp.data.api.response.MovieDetailsDto
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieState
import com.application.moviesapp.domain.model.MovieTrailerWithYoutube
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.domain.model.Stream
import com.application.moviesapp.domain.usecase.GetMovieStateInteractor
import com.application.moviesapp.domain.usecase.MovieDetailsUseCase
import com.application.moviesapp.domain.usecase.MovieStateUseCase
import com.application.moviesapp.domain.usecase.MovieTrailerUseCase
import com.application.moviesapp.domain.usecase.MovieUpdateFavouriteInteractor
import com.application.moviesapp.domain.usecase.YoutubeThumbnailUseCase
import com.application.moviesapp.domain.usecase.worker.DownloadUseCase
import com.application.moviesapp.domain.usecase.worker.VideoInfoUseCase
import com.application.moviesapp.ui.utility.getStream
import com.application.moviesapp.worker.VideoInfoWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

sealed interface DownloadUiState {
    object Default: DownloadUiState
    object Loading: DownloadUiState
    data class Complete(val videoTitle: String = "",
                        val videoThumbnail: String = "",
                        val videoStreams: List<Stream> = emptyList(),
                        val audioStreams: Stream? = null,
    ): DownloadUiState
}
@HiltViewModel
class DetailsViewModel @Inject constructor(private val useCase: MovieDetailsUseCase,
                                           private val trailerUseCase: MovieTrailerUseCase,
                                           private val updateMovieFavouriteUseCase: MovieUpdateFavouriteInteractor,
                                           private val getMovieStateUseCase: MovieStateUseCase,
                                           private val videoInfoUseCase: VideoInfoUseCase,
                                           private val downloadUseCase: DownloadUseCase): ViewModel() {

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

    val downloaderUiState: StateFlow<DownloadUiState> = videoInfoUseCase.readVideoInfo
        .map { info ->
            val videoTitle = info.outputData.getString(VideoInfoWorker.VIDEO_TITLE)
            val videoThumbnail = info.outputData.getString(VideoInfoWorker.VIDEO_THUMBNAIL)
            val videoStreams = info.outputData.getString(VideoInfoWorker.VIDEO_STREAMS)
            val audioStreams = info.outputData.getString(VideoInfoWorker.AUDIO_STREAMS)

            when {
                info.state.isFinished && !videoTitle.isNullOrEmpty() && !videoThumbnail.isNullOrEmpty() && !videoStreams.isNullOrEmpty() && !audioStreams.isNullOrEmpty() -> {
                    DownloadUiState.Complete(
                        videoTitle = videoTitle,
                        videoThumbnail = videoThumbnail,
                        videoStreams = getVideoStreams(videoStreams),
                        audioStreams = getAudioStreams(audioStreams).last()
                    )
                }
                info.state == WorkInfo.State.CANCELLED -> {
                    DownloadUiState.Default
                }
                else -> DownloadUiState.Loading
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DownloadUiState.Default
        )

    private fun getVideoStreams(videoStreams: String) : List<Stream> {
        val listResult = mutableListOf<Stream>()

        videoStreams.split(",").forEach {
            listResult.add(it.getStream)
        }
        return listResult
    }

    private fun getAudioStreams(audioStreams: String) : List<Stream> {
        val listResult = mutableListOf<Stream>()

        audioStreams.split(",").forEach {
            listResult.add(it.getStream)
        }
        return listResult
    }

    fun getVideoInfo(videoId: String = "") = viewModelScope.launch {
         videoInfoUseCase.getVideoInfo("https://www.youtube.com/watch?v=$videoId")
    }

    fun videoDownload(videoId: String, videoStream: Stream, audioStream: Stream?, movieDownloadEntity: MovieDownloadEntity?) = viewModelScope.launch {
        downloadUseCase("https://www.youtube.com/watch?v=$videoId", videoStream, audioStream, movieDownloadEntity)
    }
}