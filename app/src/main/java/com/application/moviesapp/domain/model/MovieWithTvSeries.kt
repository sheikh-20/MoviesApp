package com.application.moviesapp.domain.model

data class MovieWithTvSeries(val movies: List<MovieNowPlaying?>?,
                             val genres: MovieGenre?,
                             val tvSeries: List<TvSeriesNowPlaying?>?) {

    private val genreList = mutableSetOf<MovieGenre.Genre?>()
    val titleGenre get() = genreList.map { it?.name }.joinToString(", ")

    init {
        movies?.first()?.genreIds?.forEach {  genreId ->
            genres?.genres?.forEach { genre ->
                if (genreId?.equals(genre?.id) == true) {
                    genreList.add(genre)
                }
            }
        }
    }
}
