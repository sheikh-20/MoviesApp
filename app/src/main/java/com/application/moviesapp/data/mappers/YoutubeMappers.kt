package com.application.moviesapp.data.mappers

import com.application.moviesapp.data.api.response.YoutubeThumbnailDto
import com.application.moviesapp.domain.model.YoutubeThumbnail

fun YoutubeThumbnailDto.toYoutubeThumbnail(): YoutubeThumbnail {
    return YoutubeThumbnail(
        items = items?.map {
            YoutubeThumbnail.Item(
                id = it?.id,
                etag = it?.etag,
                kind = it?.kind,
                contentDetails = YoutubeThumbnail.Item.ContentDetails(
                    caption = it?.contentDetails?.caption,
                    definition = it?.contentDetails?.definition,
                    dimension = it?.contentDetails?.dimension,
                    duration = it?.contentDetails?.duration,
                    licensedContent = it?.contentDetails?.licensedContent,
                    projection = it?.contentDetails?.projection
                ),
                snippet = YoutubeThumbnail.Item.Snippet(
                    categoryId = it?.snippet?.categoryId,
                    channelId = it?.snippet?.channelId,
                    channelTitle = it?.snippet?.channelTitle,
                    description = it?.snippet?.description,
                    livebroadcastContent = it?.snippet?.livebroadcastContent,
                    localized = YoutubeThumbnail.Item.Snippet.Localized(
                        description = it?.snippet?.localized?.description,
                        title = it?.snippet?.localized?.title
                    ),
                    publishedAt = it?.snippet?.publishedAt,
                    thumbnails = YoutubeThumbnail.Item.Snippet.Thumbnails(
                        default = YoutubeThumbnail.Item.Snippet.Thumbnails.Default(
                            height = it?.snippet?.thumbnails?.default?.height,
                            url = it?.snippet?.thumbnails?.default?.url,
                            width = it?.snippet?.thumbnails?.default?.width
                        ),
                        high = YoutubeThumbnail.Item.Snippet.Thumbnails.High(
                            height = it?.snippet?.thumbnails?.high?.height,
                            url = it?.snippet?.thumbnails?.high?.url,
                            width = it?.snippet?.thumbnails?.high?.width
                        ),
                        maxres = YoutubeThumbnail.Item.Snippet.Thumbnails.Maxres(
                            height = it?.snippet?.thumbnails?.maxres?.height,
                            url = it?.snippet?.thumbnails?.maxres?.url,
                            width = it?.snippet?.thumbnails?.maxres?.width
                        ),
                        medium = YoutubeThumbnail.Item.Snippet.Thumbnails.Medium(
                            height = it?.snippet?.thumbnails?.medium?.height,
                            url = it?.snippet?.thumbnails?.medium?.url,
                            width = it?.snippet?.thumbnails?.medium?.width
                        ),
                        standard = YoutubeThumbnail.Item.Snippet.Thumbnails.Standard(
                            height = it?.snippet?.thumbnails?.standard?.height,
                            url = it?.snippet?.thumbnails?.standard?.url,
                            width = it?.snippet?.thumbnails?.standard?.width
                        )
                    ),
                    title = it?.snippet?.title
                )
            )
        }
    )
}