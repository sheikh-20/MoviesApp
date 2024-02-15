package com.application.moviesapp.domain.model

data class YoutubeThumbnail(
    val items: List<Item?>?
) {

    data class Item(
        val contentDetails: ContentDetails?,
        val etag: String?,
        val id: String?,
        val kind: String?,
        val snippet: Snippet?
    ) {

        data class ContentDetails(
            val caption: String?,
            val definition: String?,
            val dimension: String?,
            val duration: String?,
            val licensedContent: Boolean?,
            val projection: String?
        )

        data class Snippet(
            val categoryId: String?,
            val channelId: String?,
            val channelTitle: String?,
            val description: String?,
            val livebroadcastContent: String?,
            val localized: Localized?,
            val publishedAt: String?,
            val thumbnails: Thumbnails?,
            val title: String?
        ) {

            data class Localized(
                val description: String?,
                val title: String?
            )

            data class Thumbnails(
                val default: Default?,
                val high: High?,
                val maxres: Maxres?,
                val medium: Medium?,
                val standard: Standard?
            ) {

                data class Default(
                    val height: Int?,
                    val url: String?,
                    val width: Int?
                )

                data class High(
                    val height: Int?,
                    val url: String?,
                    val width: Int?
                )

                data class Maxres(
                    val height: Int?,
                    val url: String?,
                    val width: Int?
                )
                data class Medium(
                    val height: Int?,
                    val url: String?,
                    val width: Int?
                )

                data class Standard(
                    val height: Int?,
                    val url: String?,
                    val width: Int?
                )
            }
        }
    }
}