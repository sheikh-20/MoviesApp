package com.application.moviesapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YoutubeThumbnailDto(
    @SerialName("etag")
    val etag: String? = null,

    @SerialName("items")
    val items: List<Item?>? = null,

    @SerialName("kind")
    val kind: String? = null,

    @SerialName("pageInfo")
    val pageInfo: PageInfo? = null
) {

    @Serializable
    data class Item(
        @SerialName("contentDetails")
        val contentDetails: ContentDetails? = null,

        @SerialName("etag")
        val etag: String? = null,

        @SerialName("id")
        val id: String? = null,

        @SerialName("kind")
        val kind: String? = null,

        @SerialName("snippet")
        val snippet: Snippet? = null
    ) {

        @Serializable
        data class ContentDetails(

            @SerialName("caption")
            val caption: String? = null,

            @SerialName("contentRating")
            val contentRating: ContentRating? = null,

            @SerialName("definition")
            val definition: String? = null,

            @SerialName("dimension")
            val dimension: String? = null,

            @SerialName("duration")
            val duration: String? = null,

            @SerialName("licensedContent")
            val licensedContent: Boolean? = null,

            @SerialName("projection")
            val projection: String? = null
        ) {

            @Serializable
            class ContentRating
        }

        @Serializable
        data class Snippet(
            @SerialName("categoryId")
            val categoryId: String? = null,

            @SerialName("channelId")
            val channelId: String? = null,

            @SerialName("channelTitle")
            val channelTitle: String? = null,

            @SerialName("description")
            val description: String? = null,

            @SerialName("liveBroadcastContent")
            val livebroadcastContent: String? = null,

            @SerialName("localized")
            val localized: Localized? = null,

            @SerialName("publishedAt")
            val publishedAt: String? = null,

            @SerialName("thumbnails")
            val thumbnails: Thumbnails? = null,

            @SerialName("title")
            val title: String? = null
        ) {

            @Serializable
            data class Localized(

                @SerialName("description")
                val description: String? = null,

                @SerialName("title")
                val title: String? = null
            )

            @Serializable
            data class Thumbnails(

                @SerialName("default")
                val default: Default? = null,

                @SerialName("high")
                val high: High? = null,

                @SerialName("maxres")
                val maxres: Maxres? = null,

                @SerialName("medium")
                val medium: Medium? = null,

                @SerialName("standard")
                val standard: Standard? = null
            ) {

                @Serializable
                data class Default(
                    @SerialName("height")
                    val height: Int? = null,

                    @SerialName("url")
                    val url: String? = null,

                    @SerialName("width")
                    val width: Int? = null
                )

                @Serializable
                data class High(
                    @SerialName("height")
                    val height: Int? = null,

                    @SerialName("url")
                    val url: String? = null,

                    @SerialName("width")
                    val width: Int? = null
                )

                @Serializable
                data class Maxres(
                    @SerialName("height")
                    val height: Int? = null,

                    @SerialName("url")
                    val url: String? = null,

                    @SerialName("width")
                    val width: Int? = null
                )

                @Serializable
                data class Medium(
                    @SerialName("height")
                    val height: Int? = null,

                    @SerialName("url")
                    val url: String? = null,

                    @SerialName("width")
                    val width: Int? = null
                )

                @Serializable
                data class Standard(
                    @SerialName("height")
                    val height: Int? = null,

                    @SerialName("url")
                    val url: String? = null,

                    @SerialName("width")
                    val width: Int? = null
                )
            }
        }
    }

    @Serializable
    data class PageInfo(
        @SerialName("resultsPerPage")
        val resultsPerPage: Int? = null,

        @SerialName("totalResults")
        val totalResults: Int? = null
    )
}