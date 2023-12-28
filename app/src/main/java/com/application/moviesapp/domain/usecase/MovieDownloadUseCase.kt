package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.data.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MovieDownloadUseCase {
    fun readMovieDownload(search: String = ""): Flow<List<MovieDownloadEntity>>

    suspend fun deleteMovieDownload(download: MovieDownloadEntity)
}

class GetMovieDownloadInteractor @Inject constructor(private val repository: MoviesRepository): MovieDownloadUseCase {
    override fun readMovieDownload(search: String): Flow<List<MovieDownloadEntity>> = repository.readMovieDownload(search)

    override suspend fun deleteMovieDownload(download: MovieDownloadEntity) = repository.deleteMovieDownload(download)
}