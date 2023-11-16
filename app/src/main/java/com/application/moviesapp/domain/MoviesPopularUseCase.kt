package com.application.moviesapp.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.mappers.toMoviesEntity
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MoviesPopular
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MoviesPopularUseCase {
   operator fun invoke(): Flow<PagingData<MoviesPopular>>
}

class MoviesPopularInteractor @Inject constructor(private val repository: MoviesRepository): MoviesPopularUseCase {
    override fun invoke(): Flow<PagingData<MoviesPopular>> {
        return repository.getPopularMoviesPagingFlow().map {
            it.map { movies ->
                movies.toMovies()
            }
        }
    }
}