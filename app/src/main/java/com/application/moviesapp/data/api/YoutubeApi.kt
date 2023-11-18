package com.application.moviesapp.data.api

import com.application.moviesapp.data.api.response.YoutubeThumbnailDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
    @GET("youtube/v3/videos")
    suspend fun videoThumbnail(@Query("part") vararg part: String, @Query("id") id: String): Response<YoutubeThumbnailDto>
}