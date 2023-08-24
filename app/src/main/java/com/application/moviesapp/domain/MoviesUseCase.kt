package com.application.moviesapp.domain

import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MoviesUseCase {
    suspend operator fun invoke(): MoviesWithNewReleases
}
class GetMoviesWithNewReleaseInteractor @Inject constructor(private val moviesRepository: MoviesRepository): MoviesUseCase {

    override suspend operator fun invoke(): MoviesWithNewReleases =
        withContext(Dispatchers.Default) {
            val topRated = async { moviesRepository.getMoviesTopRated() }
            val newReleases = async { moviesRepository.getNewReleasesList() }
            val genres = async { moviesRepository.getMoviesGenreList() }

            return@withContext MoviesWithNewReleases(topRatedResponse = topRated.await(), genreResponse = genres.await(), newReleasesResponse = newReleases.await())
        }
}