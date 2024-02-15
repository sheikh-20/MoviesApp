package com.application.moviesapp.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.mappers.toMovie
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieNowPlaying
import com.application.moviesapp.domain.model.MovieSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

interface MovieSearchUseCase {
    operator fun invoke(search: String = ""): Flow<PagingData<MovieSearch>>
}

class GetMovieSearchInteractor @Inject constructor(private val repository: MoviesRepository): MovieSearchUseCase {
    override fun invoke(search: String): Flow<PagingData<MovieSearch>> = repository.getMovieBySearchPagingFlow(search).map {
        it.map { movie ->
            movie.toMovie()
        }
    }
}