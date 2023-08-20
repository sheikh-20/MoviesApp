package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryResponse(
    @SerialName("english_name")
    val englishName: String?,

    @SerialName("iso_3166_1")
    val iso: String?,

    @SerialName("native_name")
    val nativeName: String?
)