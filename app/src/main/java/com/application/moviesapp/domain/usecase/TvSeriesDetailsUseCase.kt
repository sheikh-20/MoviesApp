package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.TvSeriesDetail
import timber.log.Timber
import javax.inject.Inject

interface TvSeriesDetailsUseCase {
    suspend operator fun invoke(tvSeriesId: Int): Resource<TvSeriesDetail>
}

class GetTvSeriesDetailsInteractor @Inject constructor(private val repository: MoviesRepository): TvSeriesDetailsUseCase {

   private companion object {
       const val TAG = "GetTvSeriesInteractor"
   }

    override suspend fun invoke(tvSeriesId: Int): Resource<TvSeriesDetail> {
        return try {
            Resource.Loading

            val tvSeriesResponse = repository.getTvSeriesDetailById(tvSeriesId)

            if (tvSeriesResponse.isSuccessful) {
                val tvSeriesDetail = TvSeriesDetail(
                    adult = tvSeriesResponse.body()?.adult,
                    backdropPath = tvSeriesResponse.body()?.backdropPath,
                    createdBy =  tvSeriesResponse.body()?.createdBy?.map { TvSeriesDetail.CreatedBy(id = it?.id, creditId = it?.creditId, gender = it?.gender, name = it?.name, profilePath = it?.profilePath) },
                    episodeRunTime =  tvSeriesResponse.body()?.episodeRunTime,
                    firstAirDate =  tvSeriesResponse.body()?.firstAirDate,
                    genres =  tvSeriesResponse.body()?.genres?.map { TvSeriesDetail.Genre(id = it?.id, name = it?.name) },
                    homepage =  tvSeriesResponse.body()?.homepage,
                    id =  tvSeriesResponse.body()?.id,
                    inProduction =  tvSeriesResponse.body()?.inProduction,
                    languages =  tvSeriesResponse.body()?.languages,
                    lastAirDate =  tvSeriesResponse.body()?.lastAirDate,
                    lastEpisodeToAir =  tvSeriesResponse.body()?.lastEpisodeToAir?.run { TvSeriesDetail.LastEpisodeToAir(airDate, episodeNumber, episodeType, id, name, overview, productionCode, runtime, seasonNumber, showId, stillPath, voteAverage, voteCount) },
                    name =  tvSeriesResponse.body()?.name,
                    networks =  tvSeriesResponse.body()?.networks?.map { TvSeriesDetail.Network(id = it?.id, logoPath = it?.logoPath, name = it?.name, originCountry = it?.originCountry) },
                    nextEpisodeToAir =  tvSeriesResponse.body()?.nextEpisodeToAir?.run { TvSeriesDetail.NextEpisodeToAir(airDate, episodeNumber, episodeType, id, name, overview, productionCode, runtime, seasonNumber, showId, stillPath, voteAverage, voteCount) },
                    numberOfEpisodes =  tvSeriesResponse.body()?.numberOfEpisodes,
                    numberOfSeasons =  tvSeriesResponse.body()?.numberOfSeasons,
                    originalLanguage =  tvSeriesResponse.body()?.originalLanguage,
                    originCountry =  tvSeriesResponse.body()?.originCountry,
                    originalName =  tvSeriesResponse.body()?.originalName,
                    overview =  tvSeriesResponse.body()?.overview,
                    popularity =  tvSeriesResponse.body()?.popularity,
                    posterPath =  tvSeriesResponse.body()?.posterPath,
                    productionCompanies =  tvSeriesResponse.body()?.productionCompanies?.map { TvSeriesDetail.ProductionCompany(id = it?.id, logoPath = it?.logoPath, name = it?.name, originCountry = it?.originCountry) },
                    productionCountries =  tvSeriesResponse.body()?.productionCountries?.map { TvSeriesDetail.ProductionCountry(iso = it?.iso, name = it?.name) },
                    seasons =  tvSeriesResponse.body()?.seasons?.map { TvSeriesDetail.Season(airdate = it?.airdate, episodeCount = it?.episodeCount, id = it?.id, name = it?.name, overview = it?.overview, posterPath = it?.posterPath, seasonNumber = it?.seasonNumber, voteAverage = it?.voteAverage) },
                    spokenLanguages =  tvSeriesResponse.body()?.spokenLanguages?.map { TvSeriesDetail.SpokenLanguage(iso = it?.iso, name = it?.name, englishName = it?.englishName) },
                    status =  tvSeriesResponse.body()?.status,
                    tagline =  tvSeriesResponse.body()?.tagline,
                    type =  tvSeriesResponse.body()?.type,
                    voteAverage =  tvSeriesResponse.body()?.voteAverage,
                    voteCount =  tvSeriesResponse.body()?.voteCount
                )

                Resource.Success(tvSeriesDetail)
            } else if (tvSeriesResponse.code() == 400 || tvSeriesResponse.code() == 401 || tvSeriesResponse.code() == 404) {
                Timber.tag(TAG).e("404 error")
                Resource.Failure(Throwable())
            } else {
                Timber.tag(TAG).e("Server error")
                Resource.Failure(Throwable())
            }
        } catch (throwable: Throwable) {
            Timber.tag(TAG).e(throwable)
            Resource.Failure(throwable)
        }
    }
}