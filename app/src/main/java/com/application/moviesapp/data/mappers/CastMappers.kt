package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.api.response.CastDetailDto
import com.application.moviesapp.data.api.response.CastImagesDto
import com.application.moviesapp.domain.model.CastDetail
import com.application.moviesapp.domain.model.CastImages

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