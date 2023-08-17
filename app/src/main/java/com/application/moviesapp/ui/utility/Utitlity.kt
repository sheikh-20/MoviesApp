package com.application.moviesapp.ui.utility

import com.application.moviesapp.BuildConfig

val String.toImageUrl: String
    get() {
        return BuildConfig.IMAGE_BASE_URL + this
    }
