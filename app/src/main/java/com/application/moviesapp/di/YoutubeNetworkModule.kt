package com.application.moviesapp.di

import com.application.moviesapp.BuildConfig
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.api.NetworkInterceptor
import com.application.moviesapp.data.api.YoutubeApi
import com.application.moviesapp.data.api.YoutubeNetworkInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YoutubeNetworkModule {
    @Provides
    @Singleton
    @Named("youtube_api_key")
    fun providesApiKey(): String {
        return BuildConfig.YOUTUBE_API_KEY
    }

    @Provides
    @Singleton
    @Named("youtube_retrofit_builder")
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(BuildConfig.YOUTUBE_BASE_URL)
            .addConverterFactory(Json{ ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
    }

    @Provides
    @Singleton
    @Named("youtube_http_client")
    fun providesOKHttpClient(networkInterceptor: YoutubeNetworkInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(networkInterceptor).build()
    }

    @Provides
    @Singleton
    fun providesYoutubeApi(@Named("youtube_http_client") okHttpClient: OkHttpClient, @Named("youtube_retrofit_builder") retrofitBuilder: Retrofit.Builder): YoutubeApi {
        return retrofitBuilder.client(okHttpClient).build().create(YoutubeApi::class.java)
    }
}