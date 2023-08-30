package com.application.moviesapp.di

import android.app.Application
import com.application.moviesapp.BuildConfig
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.api.NetworkInterceptor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesApiKey(): String {
        return BuildConfig.API_KEY
    }

    @Provides
    @Singleton
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    }

    @Provides
    @Singleton
    fun providesOKHttpClient(networkInterceptor: NetworkInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(networkInterceptor).build()
    }

    @Provides
    @Singleton
    fun providesMoviesApi(okHttpClient: OkHttpClient, retrofitBuilder: Retrofit.Builder): MoviesApi {
        return retrofitBuilder.client(okHttpClient).build().create(MoviesApi::class.java)
    }

    @Provides
    @Singleton
    fun providesApplicationContext(application: Application) = application.applicationContext
}