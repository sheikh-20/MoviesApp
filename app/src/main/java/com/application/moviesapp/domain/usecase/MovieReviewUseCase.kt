package com.application.moviesapp.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.mappers.toDomain
import com.application.moviesapp.data.mappers.toMovie
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieReview
import com.application.moviesapp.domain.model.MovieSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MovieReviewUseCase {
    operator fun invoke(movieId: Int): Flow<PagingData<MovieReview>>
}

class GetMovieReviewInteractor @Inject constructor(private val repository: MoviesRepository): MovieReviewUseCase {
    override fun invoke(movieId: Int): Flow<PagingData<MovieReview>> = repository.getMovieReviewPagingFlow(movieId).map {
        it.map { movie ->
            movie.toDomain()
        }
    }
}