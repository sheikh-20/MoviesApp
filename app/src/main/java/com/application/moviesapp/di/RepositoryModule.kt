package com.application.moviesapp.di

import com.application.moviesapp.data.repository.AuthRepo
import com.application.moviesapp.data.repository.AuthRepoImpl
import com.application.moviesapp.data.repository.AuthRepository
import com.application.moviesapp.data.repository.AuthRepositoryImpl
import com.application.moviesapp.data.repository.FacebookRepoImpl
import com.application.moviesapp.data.repository.GoogleRepoImpl
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.data.repository.MoviesRepositoryImpl
import com.application.moviesapp.data.repository.YoutubeRepository
import com.application.moviesapp.data.repository.YoutubeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesMoviesRepositoryImpl(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    abstract fun providesAuthRepositoryImpl(authRepositoryImpl: AuthRepositoryImpl): AuthRepository


    //Google
    @Binds
    @Named("GoogleRepo")
    abstract fun providesGoogleRepoImpl(googleRepoImpl: GoogleRepoImpl): AuthRepo

    //Github
    @Binds
    @Named("GithubRepo")
    abstract fun providesGithubRepoImpl(authRepoImpl: AuthRepoImpl): AuthRepo


    //Facebook
    @Binds
    @Named("FacebookRepo")
    abstract fun providesFacebookRepoImpl(facebookRepoImpl: FacebookRepoImpl): AuthRepo

    @Binds
    abstract fun providesYoutubeRepositoryImpl(youtubeRepositoryImpl: YoutubeRepositoryImpl): YoutubeRepository

}