package com.application.moviesapp.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MoviesPopularUseCase {
   operator fun invoke(): Flow<PagingData<Movies>>
}

class MoviesPopularInteractor @Inject constructor(private val repository: MoviesRepository): MoviesPopularUseCase {
    override fun invoke(): Flow<PagingData<Movies>> {
        return repository.getMoviesPagingFlow().map {
            it.map { moviesEntity ->
                moviesEntity.toMovies()
            }
        }
    }
}