package com.application.moviesapp.di

import com.application.moviesapp.data.repository.AuthRepository
import com.application.moviesapp.data.repository.AuthRepositoryImpl
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.data.repository.MoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesMoviesRepositoryImpl(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    abstract fun providesAuthRepositoryImpl(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}