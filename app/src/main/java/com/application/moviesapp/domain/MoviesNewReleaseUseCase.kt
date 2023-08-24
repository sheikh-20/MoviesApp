package com.application.moviesapp.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.local.entity.MovieNewReleaseEntity
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieNewRelease
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MoviesNewReleaseUseCase {
    operator fun invoke(): Flow<PagingData<MovieNewRelease>>
}

class GetMoviesNewReleaseInteractor @Inject constructor(private val repository: MoviesRepository): MoviesNewReleaseUseCase {
    override fun invoke(): Flow<PagingData<MovieNewRelease>> = repository.getMoviesNewReleasePagingFlow().map {
        it.map { movieNewReleaseEntity ->
            movieNewReleaseEntity.toMovies()
        }
    }
}