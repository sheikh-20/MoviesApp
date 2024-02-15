package com.application.moviesapp.data

enum class SORT_BY(val title: String) {
    POPULARITY("popularity.desc"),
    LATEST_RELEASE("primary_release_date.desc"),
    VOTE_AVERAGE("vote_average.desc")
}

