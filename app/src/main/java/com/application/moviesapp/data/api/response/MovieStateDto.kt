package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MovieStateDto(
    @SerialName("favorite")
    val favorite: Boolean?,

    @SerialName("id")
    val id: Int?,

    @SerialName("rated")
    val rated: Boolean?,

    @SerialName("watchlist")
    val watchlist: Boolean?
)