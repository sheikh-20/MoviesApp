package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.api.response.TvSeriesEpisodesDto
import com.application.moviesapp.domain.model.TvSeriesEpisodes

fun TvSeriesEpisodesDto.toTvSeries(): TvSeriesEpisodes {
    return TvSeriesEpisodes(
        episodes = episodes?.map {
            TvSeriesEpisodes.Episode(
                airDate = it?.airDate,
                episodeNumber = it?.episodeNumber,
                episodeType = it?.episodeType,
                guestStars = it?.guestStars?.map { guestStar ->
                                 TvSeriesEpisodes.Episode.GuestStar(
                                     adult = guestStar?.adult,
                                     character = guestStar?.character,
                                     creditId = guestStar?.creditId,
                                     gender = guestStar?.gender,
                                     id = guestStar?.id,
                                     knownForDepartment = guestStar?.knownForDepartment,
                                     name = guestStar?.name,
                                     originalName = guestStar?.originalName,
                                     order = guestStar?.order,
                                     popularity = guestStar?.popularity,
                                     profilePath = guestStar?.profilePath
                                 )
                },
                id = it?.id,
                name = it?.name,
                overview = it?.overview,
                productionCode = it?.productionCode,
                runtime = it?.runtime,
                seasonNumber = it?.seasonNumber,
                showId = it?.showId,
                stillPath = it?.stillPath,
                voteAverage = it?.voteAverage,
                voteCount = it?.voteCount
            )
        }
    )
}