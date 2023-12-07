package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.domain.model.TvSeriesDetail
import com.application.moviesapp.domain.model.TvSeriesDetailsCast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface TvSeriesDetailsUseCase {
    suspend operator fun invoke(tvSeriesId: Int): Resource<TvSeriesDetail>
}

class GetTvSeriesDetailsInteractor @Inject constructor(private val repository: MoviesRepository): TvSeriesDetailsUseCase {

   private companion object {
       const val TAG = "GetTvSeriesInteractor"
   }

    override suspend fun invoke(tvSeriesId: Int): Resource<TvSeriesDetail> =
        withContext(Dispatchers.Default) {

            return@withContext try {

                Resource.Loading

                Timber.tag(TAG).d("invoke called!")

                val tvSeriesDetailsResponse = async { repository.getTvSeriesDetailById(tvSeriesId) }
                val tvSeriesCastResponse = async { repository.getTvSeriesDetailsCast(tvSeriesId) }
                val tvSeriesGenreResponse = async { repository.getTvSeriesGenres() }

                val tvSeriesDetails = tvSeriesDetailsResponse.await()
                val tvSeriesCast = tvSeriesCastResponse.await()
                val tvSeriesGenre = tvSeriesGenreResponse.await()

                if (tvSeriesDetails.isSuccessful && tvSeriesCast.isSuccessful && tvSeriesGenre.isSuccessful) {

                    val tvSeriesDetail = TvSeriesDetail(
                        adult = tvSeriesDetails.body()?.adult,
                        backdropPath = tvSeriesDetails.body()?.backdropPath,
                        createdBy =  tvSeriesDetails.body()?.createdBy?.map { TvSeriesDetail.CreatedBy(id = it?.id, creditId = it?.creditId, gender = it?.gender, name = it?.name, profilePath = it?.profilePath) },
                        episodeRunTime =  tvSeriesDetails.body()?.episodeRunTime,
                        firstAirDate =  tvSeriesDetails.body()?.firstAirDate,

                        genres = tvSeriesDetails.body()?.genres?.filter { genre ->
                            tvSeriesGenre.body()?.genres?.filter { genre?.equals(it?.id) == true } != null
                        }?.map { it?.name }?.joinToString(", "),

                        homepage =  tvSeriesDetails.body()?.homepage,
                        id =  tvSeriesDetails.body()?.id,
                        inProduction =  tvSeriesDetails.body()?.inProduction,
                        languages =  tvSeriesDetails.body()?.languages,
                        lastAirDate =  tvSeriesDetails.body()?.lastAirDate,
                        lastEpisodeToAir =  tvSeriesDetails.body()?.lastEpisodeToAir?.run { TvSeriesDetail.LastEpisodeToAir(airDate, episodeNumber, episodeType, id, name, overview, productionCode, runtime, seasonNumber, showId, stillPath, voteAverage, voteCount) },
                        name =  tvSeriesDetails.body()?.name,
                        networks =  tvSeriesDetails.body()?.networks?.map { TvSeriesDetail.Network(id = it?.id, logoPath = it?.logoPath, name = it?.name, originCountry = it?.originCountry) },
                        nextEpisodeToAir =  tvSeriesDetails.body()?.nextEpisodeToAir?.run { TvSeriesDetail.NextEpisodeToAir(airDate, episodeNumber, episodeType, id, name, overview, productionCode, runtime, seasonNumber, showId, stillPath, voteAverage, voteCount) },
                        numberOfEpisodes =  tvSeriesDetails.body()?.numberOfEpisodes,
                        numberOfSeasons =  tvSeriesDetails.body()?.numberOfSeasons,
                        originalLanguage =  tvSeriesDetails.body()?.originalLanguage,
                        originCountry =  tvSeriesDetails.body()?.originCountry,
                        originalName =  tvSeriesDetails.body()?.originalName,
                        overview =  tvSeriesDetails.body()?.overview,
                        popularity =  tvSeriesDetails.body()?.popularity,
                        posterPath =  tvSeriesDetails.body()?.posterPath,
                        productionCompanies =  tvSeriesDetails.body()?.productionCompanies?.map { TvSeriesDetail.ProductionCompany(id = it?.id, logoPath = it?.logoPath, name = it?.name, originCountry = it?.originCountry) },
                        productionCountries =  tvSeriesDetails.body()?.productionCountries?.map { TvSeriesDetail.ProductionCountry(iso = it?.iso, name = it?.name) },
                        seasons =  tvSeriesDetails.body()?.seasons?.map { TvSeriesDetail.Season(airdate = it?.airdate, episodeCount = it?.episodeCount, id = it?.id, name = it?.name, overview = it?.overview, posterPath = it?.posterPath, seasonNumber = it?.seasonNumber, voteAverage = it?.voteAverage) },
                        spokenLanguages =  tvSeriesDetails.body()?.spokenLanguages?.map { TvSeriesDetail.SpokenLanguage(iso = it?.iso, name = it?.name, englishName = it?.englishName) },
                        status =  tvSeriesDetails.body()?.status,
                        tagline =  tvSeriesDetails.body()?.tagline,
                        type =  tvSeriesDetails.body()?.type,
                        voteAverage =  tvSeriesDetails.body()?.voteAverage,
                        voteCount =  tvSeriesDetails.body()?.voteCount,
                        cast = tvSeriesCast.body()?.cast?.map { TvSeriesDetail.Cast(adult = it?.adult, gender = it?.gender, id = it?.id, knownForDepartment = it?.knownForDepartment, name = it?.name, order = it?.order, originalName = it?.originalName, popularity = it?.popularity, profilePath = it?.profilePath, roles = it?.roles?.map { roles -> TvSeriesDetail.Cast.Role(character = roles?.character, creditId = roles?.creditId, episodeCount = roles?.episodeCount) }, totalEpisodeCount = it?.totalEpisodeCount) },
                        crew = tvSeriesCast.body()?.crew?.map { TvSeriesDetail.Crew(adult = it?.adult, gender = it?.gender, id = it?.id, knownForDepartment = it?.knownForDepartment, name = it?.name, originalName = it?.originalName, popularity = it?.popularity, profilePath = it?.profilePath, department = it?.department, jobs = it?.jobs?.map { jobs -> TvSeriesDetail.Crew.Job(job = jobs?.job, creditId = jobs?.job, episodeCount = jobs?.episodeCount) }, totalEpisodeCount = it?.totalEpisodeCount) }
                    )

                    Resource.Success(tvSeriesDetail)

                }  else if (tvSeriesDetails.code() == 400 || tvSeriesDetails.code() == 401 || tvSeriesDetails.code() == 404) {
                    Timber.tag(TAG).e("404 error")
                    Resource.Failure(Throwable())
                } else {
                    Timber.tag(TAG).e("Server error")
                    Resource.Failure(Throwable())
                }

            } catch (throwable: Throwable) {
                Resource.Failure(throwable)
            }
        }
}