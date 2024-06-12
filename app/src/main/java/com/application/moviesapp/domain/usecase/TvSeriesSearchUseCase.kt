package com.application.moviesapp.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.mappers.toMovie
import com.application.moviesapp.data.mappers.toTvSeries
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieSearch
import com.application.moviesapp.domain.model.TvSeriesSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface TvSeriesSearchUseCase {
    operator fun invoke(search: String = ""): Flow<PagingData<TvSeriesSearch>>
}

class GetTvSeriesSearchInteractor @Inject constructor(private val repository: MoviesRepository): TvSeriesSearchUseCase {
    override fun invoke(search: String): Flow<PagingData<TvSeriesSearch>> = repository.getTvSeriesBySearchPagingFlow(search).map {
        it.map { tvSeries -> tvSeries.toTvSeries() }
    }
}