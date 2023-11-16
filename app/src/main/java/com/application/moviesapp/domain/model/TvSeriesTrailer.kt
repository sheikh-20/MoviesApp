package com.application.moviesapp.domain.model

data class TvSeriesTrailer(
    val id: Int?,
    val results: List<Result?>?
) {

    data class Result(
        val id: String?,
        val isoOne: String?,
        val isoTwo: String?,
        val key: String?,
        val name: String?,
        val official: Boolean?,
        val publishedAt: String?,
        val site: String?,
        val size: Int?,
        val type: String?
    )
}