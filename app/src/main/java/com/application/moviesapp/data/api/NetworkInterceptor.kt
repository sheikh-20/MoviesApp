package com.application.moviesapp.data.api

import com.application.moviesapp.BuildConfig
import kotlinx.coroutines.delay
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

class NetworkInterceptor @Inject constructor(@Named("movies_api_key") private val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader(name = "Authorization", value = "Bearer $apiKey")

        try {
            Thread.sleep(2_000L) // Introduce delay
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return chain.proceed(request.build())
    }
}

class YoutubeNetworkInterceptor @Inject constructor(@Named("youtube_api_key") private val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().url.newBuilder()
        request.addQueryParameter(name = "key", value = apiKey)

        try {
            Thread.sleep(2_000L) // Introduce delay
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return chain.proceed(chain.request().newBuilder().url(request.build()).build())
    }
}
