package com.application.moviesapp.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.mappers.toDomain
import com.application.moviesapp.data.remote.TvSeriesReviewPagingSource
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieReview
import com.application.moviesapp.domain.model.TvSeriesReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface TvSeriesReviewUseCase {
    operator fun invoke(seriesId: Int): Flow<PagingData<TvSeriesReview>>
}

class GetTvSeriesReviewInteractor @Inject constructor(private val repository: MoviesRepository): TvSeriesReviewUseCase {
    override fun invoke(seriesId: Int): Flow<PagingData<TvSeriesReview>> = repository.getTvSeriesReviewPagingFlow(seriesId).map {
        it.map { tvSeries ->
            tvSeries.toDomain()
        }
    }
}