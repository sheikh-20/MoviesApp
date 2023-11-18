package com.application.moviesapp.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.application.moviesapp.data.mappers.toMovies
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MoviesDiscover
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MoviesDiscoverUseCase {
   operator fun invoke(genre: String = "", sortBy: String = "", includeAdult: Boolean = false): Flow<PagingData<MoviesDiscover>>
}

class MoviesDiscoverInteractor @Inject constructor(private val repository: MoviesRepository): MoviesDiscoverUseCase {
    override fun invoke(genre: String, sortBy: String, includeAdult: Boolean): Flow<PagingData<MoviesDiscover>> {
        return repository.getDiscoverMoviesPagingFlow(genre, sortBy, includeAdult).map {
            it.map { movies ->
                movies.toMovies()
            }
        }
    }
}