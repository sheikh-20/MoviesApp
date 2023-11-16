package com.application.moviesapp.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.TvSeriesNowPlaying
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface TvSeriesNowPlayingUseCase {
    operator fun invoke(): Flow<PagingData<TvSeriesNowPlaying>>
}

class GetTvSeriesNowPlayingInteractor @Inject constructor(private val moviesRepository: MoviesRepository): TvSeriesNowPlayingUseCase {
    override fun invoke(): Flow<PagingData<TvSeriesNowPlaying>> = moviesRepository.getTvSeriesNowPlayingPagingFlow().map {
        it.map { tvSeriesDto ->
            tvSeriesDto.toMovies()
        }
    }
}