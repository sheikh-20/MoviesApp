package com.application.moviesapp.ui.utility

import androidx.compose.foundation.lazy.LazyListState
import com.application.moviesapp.BuildConfig

val String.toImageUrl: String
    get() {
        return BuildConfig.IMAGE_BASE_URL + this
    }


val String.toYoutubeDuration: String
    get() {
        val pattern = "\\d+H|\\d+M|\\d+S"
        val regex = Regex(pattern)

        val matches = regex.findAll(this)

        return matches.map {
            it.value.lowercase()
        }.joinToString(" ")
    }

val Double.toOneDecimal: String
    get() {
        return "%.1f".format(this)
    }

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0