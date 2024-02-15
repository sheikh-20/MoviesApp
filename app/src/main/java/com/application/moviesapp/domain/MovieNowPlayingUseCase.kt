package com.application.moviesapp.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieNowPlaying
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MoviesNowPlayingUseCase {
    operator fun invoke(): Flow<PagingData<MovieNowPlaying>>
}

class GetMoviesNowPlayingInteractor @Inject constructor(private val repository: MoviesRepository): MoviesNowPlayingUseCase {
    override fun invoke(): Flow<PagingData<MovieNowPlaying>> = repository.getMoviesNowPlayingPagingFlow().map {
        it.map { movieNowPlayingEntity ->
            movieNowPlayingEntity.toMovies()
        }
    }
}