package com.application.moviesapp.domain

import com.application.moviesapp.data.api.response.CountryResponse
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.mock.Categories
import com.application.moviesapp.data.mock.Period
import com.application.moviesapp.data.mock.Sort

data class MoviesWithSort(
    val categories: List<Categories>,
    val genres: MovieGenreResponse,
    val region: List<CountryResponse>,
    val period: List<Period>,
    val sort: List<Sort>
)