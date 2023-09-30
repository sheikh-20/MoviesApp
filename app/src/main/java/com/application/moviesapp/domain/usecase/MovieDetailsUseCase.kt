package com.application.moviesapp.domain.usecase

import android.graphics.Movie
import com.application.moviesapp.data.api.response.MovieDetailsCastDto
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MoviesDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

interface MovieDetailsUseCase {
    suspend operator fun invoke(movieId: Int): Resource<MoviesDetail>
}

class GetMovieDetailInteractor @Inject constructor(private val moviesRepository: MoviesRepository): MovieDetailsUseCase {

    companion object {
        private const val TAG = "GetMovieDetailInteractor"
    }

    override suspend fun invoke(movieId: Int): Resource<MoviesDetail> =
         withContext(Dispatchers.Default) {

            return@withContext try {

                Timber.tag(TAG).d("invoke called!")

                val movieDetails = async { moviesRepository.getMoviesDetailById(movieId) }
                val movieCast = async { moviesRepository.getMovieDetailsCast(movieId) }
                val movieGenre = async { moviesRepository.getMoviesGenreList() }

                val movieDetailsOutput = movieDetails.await()
                val movieCastOutput = movieCast.await()
                val movieGenreOutput = movieGenre.await()


                if (movieDetailsOutput.isSuccessful && movieCastOutput.isSuccessful) {

                    val result = MoviesDetail(
                        adult = movieDetailsOutput.body()?.adult,
                        backdropPath = movieDetailsOutput.body()?.backdropPath,
                        belongsToCollection = MoviesDetail.Collection(id = movieDetailsOutput.body()?.belongsToCollection?.id, name = movieDetailsOutput.body()?.belongsToCollection?.name, posterPath = movieDetailsOutput.body()?.belongsToCollection?.posterPath, backdropPath = movieDetailsOutput.body()?.belongsToCollection?.backdropPath),
                        budget = movieDetailsOutput.body()?.budget,


                        genres = movieDetailsOutput.body()?.genres?.filter { genre ->
                            movieGenreOutput.genres?.filter { genre?.equals(it?.id) == true } != null
                        }?.map { it?.name }?.joinToString(", "),


                        homepage = movieDetailsOutput.body()?.homepage,
                        id = movieDetailsOutput.body()?.id,
                        imdbId = movieDetailsOutput.body()?.imdbId,
                        originalLanguage = movieDetailsOutput.body()?.originalLanguage,
                        originalTitle = movieDetailsOutput.body()?.originalTitle,
                        overview = movieDetailsOutput.body()?.overview,
                        popularity = movieDetailsOutput.body()?.popularity,
                        posterPath = movieDetailsOutput.body()?.posterPath,
                        productionCompanies = movieDetailsOutput.body()?.productionCompanies?.map { MoviesDetail.ProductionCompany(id = it?.id, logoPath = it?.logoPath, name = it?.name, originCountry = it?.originCountry) },
                        productionCountries = movieDetailsOutput.body()?.productionCountries?.map { MoviesDetail.ProductionCountry(iso = it?.iso, name = it?.name) },
                        releaseDate = movieDetailsOutput.body()?.releaseDate,
                        revenue = movieDetailsOutput.body()?.revenue,
                        runtime = movieDetailsOutput.body()?.runtime,
                        spokenLanguages = movieDetailsOutput.body()?.spokenLanguages?.map { MoviesDetail.SpokenLanguage(englishName = it?.englishName, iso = it?.iso, name = it?.name) },
                        status = movieDetailsOutput.body()?.status,
                        tagline = movieDetailsOutput.body()?.tagline,
                        title = movieDetailsOutput.body()?.title,
                        video = movieDetailsOutput.body()?.video,
                        voteAverage = movieDetailsOutput.body()?.voteAverage,
                        voteCount = movieDetailsOutput.body()?.voteCount,
                        cast = movieCastOutput.body()?.cast?.map { MoviesDetail.Cast(adult = it?.adult, castId = it?.castId, character = it?.character, creditId = it?.creditId, gender = it?.gender, id = it?.id, knownForDepartment = it?.knownForDepartment, name = it?.name, order = it?.order, originalName = it?.originalName, popularity = it?.popularity, profilePath = it?.profilePath) },
                        crew = movieCastOutput.body()?.crew?.map { MoviesDetail.Crew(adult = it?.adult, creditId = it?.creditId, gender = it?.gender, id = it?.id, knownForDepartment = it?.knownForDepartment, name = it?.name, originalName = it?.originalName, popularity = it?.popularity, profilePath = it?.profilePath, job = it?.job, department = it?.department) }
                    )

                    Resource.Success(result)
                } else {
                    Timber.tag(TAG).e("Client error")
                    Resource.Failure(Throwable())
                }

            } catch (throwable: Throwable) {
                Resource.Failure(throwable)
            }
        }
}