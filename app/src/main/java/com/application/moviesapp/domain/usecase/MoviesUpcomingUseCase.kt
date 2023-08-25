package com.application.moviesapp.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieUpcoming
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MoviesUpcomingUseCase {
    operator fun invoke(): Flow<PagingData<MovieUpcoming>>
}

class MoviesUpcomingInterator @Inject constructor(private val moviesRepository: MoviesRepository): MoviesUpcomingUseCase {
    override fun invoke(): Flow<PagingData<MovieUpcoming>> = moviesRepository.getMoviesUpcomingPagingFlow().map {
        it.map { movieUpcomingEntity ->
            movieUpcomingEntity.toMovies()
        }
    }
}