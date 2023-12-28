package com.application.moviesapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.domain.usecase.MovieDownloadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class DownloadsUiState(val data: List<MovieDownloadEntity> = emptyList())
@HiltViewModel
class DownloadViewModel @Inject constructor(private val useCase: MovieDownloadUseCase): ViewModel() {

    fun readAllDownload(search: String = "") = useCase.readMovieDownload(search).map { DownloadsUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = DownloadsUiState()
        )

    fun deleteMovieDownload(downloadEntity: MovieDownloadEntity) = viewModelScope.launch {
        useCase.deleteMovieDownload(downloadEntity)
    }
}