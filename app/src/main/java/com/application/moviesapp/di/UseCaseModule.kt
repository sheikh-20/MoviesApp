package com.application.moviesapp.di

import com.application.moviesapp.data.repository.AuthRepository
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.GetMoviesNewReleaseInteractor
import com.application.moviesapp.domain.GetMoviesWithNewReleaseInteractor
import com.application.moviesapp.domain.GetMoviesWithSortInteractor
import com.application.moviesapp.domain.MoviesNewReleaseUseCase
import com.application.moviesapp.domain.MoviesPopularInteractor
import com.application.moviesapp.domain.MoviesPopularUseCase
import com.application.moviesapp.domain.MoviesSortUseCase
import com.application.moviesapp.domain.MoviesUseCase
import com.application.moviesapp.domain.usecase.GetUserInfoInteractor
import com.application.moviesapp.domain.usecase.MoviesUpcomingInterator
import com.application.moviesapp.domain.usecase.MoviesUpcomingUseCase
import com.application.moviesapp.domain.usecase.SignInGoogleInteractor
import com.application.moviesapp.domain.usecase.SignInGoogleUseCase
import com.application.moviesapp.domain.usecase.UserInfoUseCase
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

    @Provides
    @Singleton
    fun providesMoviesPopularUseCase(moviesRepository: MoviesRepository): MoviesPopularUseCase {
        return MoviesPopularInteractor(moviesRepository)
    }

    @Provides
    @Singleton
    fun providesMoviesNewReleaseUseCase(moviesRepository: MoviesRepository): MoviesNewReleaseUseCase {
        return GetMoviesNewReleaseInteractor(moviesRepository)
    }

    @Provides
    @Singleton
    fun providesMoviesUpcomingUseCase(moviesRepository: MoviesRepository): MoviesUpcomingUseCase {
        return MoviesUpcomingInterator(moviesRepository)
    }

    @Provides
    @Singleton
    fun providesSignInGoogleUseCase(authRepository: AuthRepository): SignInGoogleUseCase {
        return SignInGoogleInteractor(authRepository)
    }

    @Provides
    @Singleton
    fun providesUserInfoUseCase(authRepository: AuthRepository): UserInfoUseCase {
        return GetUserInfoInteractor(authRepository)
    }
}