package com.application.moviesapp.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.mappers.toDomain
import com.application.moviesapp.data.mappers.toMovie
import com.application.moviesapp.data.remote.TvSeriesFavouriteDto
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MovieFavourite
import com.application.moviesapp.domain.model.TvSeriesFavourite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface TvSeriesFavouriteUseCase {
    operator fun invoke(searchText: String = ""): Flow<PagingData<TvSeriesFavourite>>
}

class GetTvSeriesFavouriteInteractor @Inject constructor(private val repository: MoviesRepository): TvSeriesFavouriteUseCase {

    private companion object {
        const val TAG = "GetTvSeriesFavouriteInteractor"
    }

    override fun invoke(searchText: String): Flow<PagingData<TvSeriesFavourite>> = repository.getFavouriteTvSeriesPagingFlow(searchText).map {
        it.map { tvSeries -> tvSeries.toDomain() }
    }
}