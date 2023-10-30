package com.application.moviesapp.di

import com.application.moviesapp.data.repository.AuthRepository
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.data.repository.SettingsPreferenceRepository
import com.application.moviesapp.data.repository.UserPreferenceRepository
import com.application.moviesapp.data.repository.YoutubeRepository
import com.application.moviesapp.domain.GetMoviesNewReleaseInteractor
import com.application.moviesapp.domain.GetMoviesWithNewReleaseInteractor
import com.application.moviesapp.domain.GetMoviesWithSortInteractor
import com.application.moviesapp.domain.MoviesNewReleaseUseCase
import com.application.moviesapp.domain.MoviesPopularInteractor
import com.application.moviesapp.domain.MoviesPopularUseCase
import com.application.moviesapp.domain.MoviesSortUseCase
import com.application.moviesapp.domain.MoviesUseCase
import com.application.moviesapp.domain.model.MovieGenre
import com.application.moviesapp.domain.usecase.GetMovieDetailInteractor
import com.application.moviesapp.domain.usecase.GetMovieFavouriteInteractor
import com.application.moviesapp.domain.usecase.GetAccountSetupInteractor
import com.application.moviesapp.domain.usecase.GetMovieStateInteractor
import com.application.moviesapp.domain.usecase.GetMovieTrailerInteractor
import com.application.moviesapp.domain.usecase.GetSignInFacebookInteractor
import com.application.moviesapp.domain.usecase.GetUserInfoInteractor
import com.application.moviesapp.domain.usecase.MovieDetailsUseCase
import com.application.moviesapp.domain.usecase.MovieFavouriteUseCase
import com.application.moviesapp.domain.usecase.AccountSetupUseCase
import com.application.moviesapp.domain.usecase.GetMovieGenreInteractor
import com.application.moviesapp.domain.usecase.GetSettingsInteractor
import com.application.moviesapp.domain.usecase.GetTvSeriesGenreInteractor
import com.application.moviesapp.domain.usecase.MovieGenresUseCase
import com.application.moviesapp.domain.usecase.MovieStateUseCase
import com.application.moviesapp.domain.usecase.MovieTrailerUseCase
import com.application.moviesapp.domain.usecase.MovieUpdateFavouriteInteractor
import com.application.moviesapp.domain.usecase.MovieUpdateFavouriteUseCase
import com.application.moviesapp.domain.usecase.MoviesUpcomingInterator
import com.application.moviesapp.domain.usecase.MoviesUpcomingUseCase
import com.application.moviesapp.domain.usecase.SettingsUseCase
import com.application.moviesapp.domain.usecase.SignInEmailInteractor
import com.application.moviesapp.domain.usecase.SignInEmailUseCase
import com.application.moviesapp.domain.usecase.SignInFacebookUseCase
import com.application.moviesapp.domain.usecase.SignInGithubInteractor
import com.application.moviesapp.domain.usecase.SignInGithubUseCase
import com.application.moviesapp.domain.usecase.SignInGoogleInteractor
import com.application.moviesapp.domain.usecase.SignInGoogleUseCase
import com.application.moviesapp.domain.usecase.SignUpEmailInteractor
import com.application.moviesapp.domain.usecase.SignUpEmailUseCase
import com.application.moviesapp.domain.usecase.TvSeriesGenreUseCase
import com.application.moviesapp.domain.usecase.UserInfoUseCase
import com.application.moviesapp.domain.usecase.YoutubeThumbnailInteractor
import com.application.moviesapp.domain.usecase.YoutubeThumbnailUseCase
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.annotation.Signed
import javax.inject.Named
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
    fun providesSignInGoogleUseCase(@Named("GoogleRepo") authRepository: AuthRepository, oneTapClient: SignInClient): SignInGoogleUseCase {
        return SignInGoogleInteractor(authRepository, oneTapClient)
    }

    @Provides
    @Singleton
    fun providesSignInGithubUseCase(@Named("GithubRepo") authRepository: AuthRepository): SignInGithubUseCase {
        return SignInGithubInteractor(authRepository)
    }

    @Provides
    @Singleton
    fun providesSignInFacebookUseCase(@Named("FacebookRepo") authRepository: AuthRepository): SignInFacebookUseCase {
        return GetSignInFacebookInteractor(authRepository)
    }

    @Provides
    @Singleton
    fun providesSignInEmailUseCase(@Named("SignInEmailRepo") authRepository: AuthRepository): SignInEmailUseCase {
        return SignInEmailInteractor(authRepository)
    }

    @Provides
    @Singleton
    fun providesSignUpEmailUseCase(@Named("SignUpEmailRepo") authRepository: AuthRepository): SignUpEmailUseCase {
        return SignUpEmailInteractor(authRepository)
    }

    @Provides
    @Singleton
    fun providesMovieDetailsUseCase(moviesRepository: MoviesRepository): MovieDetailsUseCase {
        return GetMovieDetailInteractor(moviesRepository)
    }


    @Provides
    @Singleton
    fun providesYoutubeThumbnailUseCase(youtubeRepository: YoutubeRepository): YoutubeThumbnailUseCase {
        return YoutubeThumbnailInteractor(youtubeRepository)
    }

    @Provides
    @Singleton
    fun providesMovieTrailerUseCase(moviesRepository: MoviesRepository, youtubeRepository: YoutubeRepository): MovieTrailerUseCase {
        return GetMovieTrailerInteractor(moviesRepository, youtubeRepository)
    }


    @Provides
    @Singleton
    fun providesMovieFavouriteUseCase(moviesRepository: MoviesRepository): MovieFavouriteUseCase {
        return GetMovieFavouriteInteractor(moviesRepository)
    }


    @Provides
    @Singleton
    fun providesMovieUpdateFavouriteUseCase(moviesRepository: MoviesRepository): MovieUpdateFavouriteUseCase {
        return MovieUpdateFavouriteInteractor(moviesRepository)
    }

    @Provides
    @Singleton
    fun providesMovieStateUseCase(moviesRepository: MoviesRepository): MovieStateUseCase {
        return GetMovieStateInteractor(moviesRepository)
    }

    @Provides
    @Singleton
    fun providesSetAccountSetupUseCase(userPreferenceRepository: UserPreferenceRepository): AccountSetupUseCase {
        return GetAccountSetupInteractor(userPreferenceRepository)
    }

    @Provides
    @Singleton
    fun providesMovieGenreUseCase(moviesRepository: MoviesRepository): MovieGenresUseCase {
        return GetMovieGenreInteractor(moviesRepository)
    }

    @Provides
    @Singleton
    fun providesTvSeriesGenreUseCase(moviesRepository: MoviesRepository): TvSeriesGenreUseCase {
        return GetTvSeriesGenreInteractor(moviesRepository)
    }

    @Provides
    @Singleton
    fun providesSettingsUseCase(settingsPreferenceRepository: SettingsPreferenceRepository): SettingsUseCase {
        return GetSettingsInteractor(settingsPreferenceRepository)
    }
}

