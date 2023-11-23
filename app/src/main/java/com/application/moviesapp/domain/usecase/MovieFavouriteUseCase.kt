package com.application.moviesapp.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.mappers.toMovie
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieFavourite
import com.application.moviesapp.domain.model.MovieNowPlaying
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

interface MovieFavouriteUseCase {
    operator fun invoke(): Flow<PagingData<MovieFavourite>>
}

class GetMovieFavouriteInteractor @Inject constructor(private val repository: MoviesRepository): MovieFavouriteUseCase {

    private companion object {
        const val TAG = "GetMovieFavouriteInteractor"
    }

    override fun invoke(): Flow<PagingData<MovieFavourite>> = repository.getFavouriteMoviesPagingFlow().map {
        it.map { movie ->
            movie.toMovie()
        }
    }
}