package com.application.moviesapp.di

import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.GetMoviesWithNewReleaseInteractor
import com.application.moviesapp.domain.GetMoviesWithSortInteractor
import com.application.moviesapp.domain.MoviesSortUseCase
import com.application.moviesapp.domain.MoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun providesUseCase(moviesRepository: MoviesRepository): MoviesUseCase {
        return GetMoviesWithNewReleaseInteractor(moviesRepository)
    }

    @Provides
    @Singleton
    fun providesMoviesSortUseCase(moviesRepository: MoviesRepository): MoviesSortUseCase {
        return GetMoviesWithSortInteractor(moviesRepository)
    }
}