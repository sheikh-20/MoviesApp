package com.application.moviesapp.domain

import com.application.moviesapp.data.api.response.MovieNewReleasesResponse
import com.application.moviesapp.data.api.response.MovieTopRatedResponse

data class MoviesWithNewReleases(val topRatedResponse: MovieTopRatedResponse,
                                 val newReleasesResponse: MovieNewReleasesResponse)
