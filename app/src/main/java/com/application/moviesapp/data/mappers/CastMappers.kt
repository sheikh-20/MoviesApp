package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.api.response.CastDetailDto
import com.application.moviesapp.data.api.response.CastImagesDto
import com.application.moviesapp.data.api.response.CastMovieCreditsDto
import com.application.moviesapp.data.api.response.CastTvSeriesCreditDto
import com.application.moviesapp.domain.model.CastDetail
import com.application.moviesapp.domain.model.CastImages
import com.application.moviesapp.domain.model.CastMovieCredits
import com.application.moviesapp.domain.model.CastTvSeriesCredit

fun CastDetailDto.toDomain(): CastDetail {
    return CastDetail(
        adult = adult,
        alsoKnownAs = alsoKnownAs,
        biography = biography,
        birthday =  birthday,
        deathday = deathday,
        gender = gender,
        homepage = homepage,
        id = id,
        imdbId = imdbId,
        knownForDepartment = knownForDepartment,
        name = name,
        placeOfBirth = placeOfBirth,
        popularity = popularity,
        profilePath = profilePath
    )
}

fun CastImagesDto.toDomain(): CastImages {
    return CastImages(
        id = id,
        profiles = profiles?.map { CastImages.Profile(aspectRatio = it?.aspectRatio, voteCount = it?.voteCount, voteAverage = it?.voteAverage, filePath = it?.filePath, iso = it?.iso, height = it?.height, width = it?.width) }
    )
}


fun CastMovieCreditsDto.toDomain(): CastMovieCredits {
    return CastMovieCredits(
        id = id,
        cast = cast?.map { CastMovieCredits.Cast(
            adult = it?.adult,
            backdropPath = it?.backdropPath,
            character = it?.character,
            creditId = it?.creditId,
            genreIds = it?.genreIds,
            id = it?.id,
            order = it?.order,
            originalLanguage = it?.originalLanguage,
            originalTitle = it?.originalTitle,
            overview = it?.overview,
            popularity = it?.popularity,
            posterPath = it?.posterPath,
            releaseDate = it?.releaseDate,
            title = it?.title,
            video = it?.video,
            voteAverage = it?.voteAverage,
            voteCount = it?.voteCount
        ) },
        crew = crew?.map {
            CastMovieCredits.Crew(
                adult = it?.adult,
                backdropPath = it?.backdropPath,
                creditId = it?.creditId,
                genreIds = it?.genreIds,
                id = it?.id,
                originalLanguage = it?.originalLanguage,
                originalTitle = it?.originalTitle,
                overview = it?.overview,
                popularity = it?.popularity,
                posterPath = it?.posterPath,
                releaseDate = it?.releaseDate,
                title = it?.title,
                video = it?.video,
                voteAverage = it?.voteAverage,
                voteCount = it?.voteCount,
                job = it?.job,
                department = it?.department
            )
        }
    )
}

fun CastTvSeriesCreditDto.toDomain(): CastTvSeriesCredit {
    return CastTvSeriesCredit(
        id = id,
        cast = cast?.map { CastTvSeriesCredit.Cast(
            adult = it?.adult,
            backdropPath = it?.backdropPath,
            character = it?.character,
            creditId = it?.creditId,
            episodeCount = it?.episodeCount,
            firstAirDate = it?.firstAirDate,
            genreIds = it?.genreIds,
            id = it?.id,
            name = it?.name,
            originCountry = it?.originCountry,
            originalLanguage = it?.originalLanguage,
            originalName = it?.originalName,
            overview = it?.overview,
            popularity = it?.popularity,
            posterPath = it?.posterPath,
            voteAverage = it?.voteAverage,
            voteCount = it?.voteCount
        ) },
        crew = crew?.map {
            CastTvSeriesCredit.Crew(
                adult = it?.adult,
                backdropPath = it?.backdropPath,
                creditId = it?.creditId,
                episodeCount = it?.episodeCount,
                firstAirDate = it?.firstAirDate,
                genreIds = it?.genreIds,
                id = it?.id,
                name = it?.name,
                originCountry = it?.originCountry,
                originalLanguage = it?.originalLanguage,
                originalName = it?.originalName,
                overview = it?.overview,
                popularity = it?.popularity,
                posterPath = it?.posterPath,
                voteAverage = it?.voteAverage,
                voteCount = it?.voteCount,
                department = it?.department,
                job = it?.job
            )
        }
    )
}