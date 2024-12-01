package com.application.moviesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.work.WorkInfo
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.domain.model.CastDetailWithImages
import com.application.moviesapp.domain.model.MovieState
import com.application.moviesapp.domain.model.MovieTrailerWithYoutube
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.domain.model.Stream
import com.application.moviesapp.domain.model.TvSeriesDetail
import com.application.moviesapp.domain.model.TvSeriesEpisodes
import com.application.moviesapp.domain.model.TvSeriesTrailerWithYoutube
import com.application.moviesapp.domain.usecase.CastDetailsUseCase
import com.application.moviesapp.domain.usecase.MovieDetailsUseCase
import com.application.moviesapp.domain.usecase.MovieReviewUseCase
import com.application.moviesapp.domain.usecase.MovieStateUseCase
import com.application.moviesapp.domain.usecase.MovieTrailerUseCase
import com.application.moviesapp.domain.usecase.MovieUpdateFavouriteInteractor
import com.application.moviesapp.domain.usecase.TvSeriesDetailsUseCase
import com.application.moviesapp.domain.usecase.TvSeriesEpisodesUseCase
import com.application.moviesapp.domain.usecase.TvSeriesReviewUseCase
import com.application.moviesapp.domain.usecase.TvSeriesTrailerUseCase
import com.application.moviesapp.domain.usecase.worker.DownloadUseCase
import com.application.moviesapp.domain.usecase.worker.VideoInfoUseCase
import com.application.moviesapp.ui.utility.getStream
import com.application.moviesapp.worker.VideoInfoWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
import timber.log.Timber
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
                                           private val tvSerialDetailsUseCase: TvSeriesDetailsUseCase,
                                           private val moviesTrailerUseCase: MovieTrailerUseCase,
                                           private val tvSeriesTrailerUseCase: TvSeriesTrailerUseCase,
                                           private val updateMovieFavouriteUseCase: MovieUpdateFavouriteInteractor,
                                           private val getMovieStateUseCase: MovieStateUseCase,
                                           private val videoInfoUseCase: VideoInfoUseCase,
                                           private val downloadUseCase: DownloadUseCase,
                                           private val tvSeriesEpisodesUseCase: TvSeriesEpisodesUseCase,
                                           private val castDetailsUseCase: CastDetailsUseCase,
                                           private val movieReviewUseCase: MovieReviewUseCase,
                                           private val tvSeriesReviewUseCase: TvSeriesReviewUseCase): ViewModel() {

    private companion object {
        const val TAG = "DetailsViewModel"
    }

    private var _movieDetailResponse = MutableStateFlow<Resource<MoviesDetail>>(Resource.Loading)
    val movieDetailResponse  get() = _movieDetailResponse.asStateFlow()

    private var _tvSeriesDetailResponse = MutableStateFlow<Resource<TvSeriesDetail>>(Resource.Loading)
    val tvSeriesDetailResponse get() = _tvSeriesDetailResponse.asStateFlow()


    private var _movieTrailerResponse = MutableStateFlow<Resource<List<MovieTrailerWithYoutube>>>(Resource.Loading)
    val movieTrailerResponse get() = _movieTrailerResponse.asStateFlow()

    private var _tvSeriesTrailerResponse = MutableStateFlow<Resource<List<TvSeriesTrailerWithYoutube>>>(Resource.Loading)
    val tvSeriesTrailerResponse get() = _tvSeriesTrailerResponse.asStateFlow()


    private var _movieStateResponse = MutableStateFlow<Resource<MovieState>>(Resource.Loading)
    val movieStateResponse get() = _movieStateResponse.asStateFlow()


    private var _tvSeriesEpisodesResponse = MutableStateFlow<Resource<TvSeriesEpisodes>>(Resource.Loading)
    val tvSeriesEpisodesResponse: StateFlow<Resource<TvSeriesEpisodes>> get() = _tvSeriesEpisodesResponse

    private var _castDetailsResponse = MutableStateFlow<Resource<CastDetailWithImages>>(Resource.Loading)
    val castDetailResponse: StateFlow<Resource<CastDetailWithImages>> get() = _castDetailsResponse

    fun getMovieReviewPagingFlow(movieId: Int) = movieReviewUseCase(movieId).cachedIn(viewModelScope)
    fun getTvSeriesReviewPagingFlow(seriesId: Int) = tvSeriesReviewUseCase(seriesId).cachedIn(viewModelScope)

    fun getMovieDetail(movieId: Int) = viewModelScope.launch {
        _movieDetailResponse.value = useCase(movieId)
        Timber.tag(TAG).d(movieDetailResponse.value.toString())
    }

    fun getTvSeriesDetail(tvSeriesId: Int) = viewModelScope.launch {
        _tvSeriesDetailResponse.value = tvSerialDetailsUseCase(tvSeriesId)
    }

    fun getMovieTrailer(movieId: Int) = viewModelScope.launch {
        _movieTrailerResponse.value = moviesTrailerUseCase(movieId)
        Timber.tag(TAG).d(movieTrailerResponse.value.toString())
    }

    fun getTvSeriesTrailer(seriesId: Int) = viewModelScope.launch {
        _tvSeriesTrailerResponse.value = tvSeriesTrailerUseCase(seriesId)
        Timber.tag(TAG).d(tvSeriesTrailerResponse.value.toString())
    }

    fun getTvSeriesEpisodes(seriesId: Int, seasonNumber: Int = 1) = viewModelScope.launch {
        _tvSeriesEpisodesResponse.value = tvSeriesEpisodesUseCase(seriesId, seasonNumber)
        Timber.tag(TAG).d(tvSeriesEpisodesResponse.value.toString())
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

    fun getCastDetail(personId: Int) = viewModelScope.launch {
        try {
            _castDetailsResponse.value = castDetailsUseCase(personId)
        } catch (exception: Exception) {
            Timber.tag(TAG).e(exception)
        }
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