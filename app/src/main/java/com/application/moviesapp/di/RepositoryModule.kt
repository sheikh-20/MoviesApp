package com.application.moviesapp.di

import com.application.moviesapp.data.python.DownloaderRepository
import com.application.moviesapp.data.python.DownloaderRepositoryImpl
import com.application.moviesapp.data.python.WorkManagerRepository
import com.application.moviesapp.data.python.WorkManagerRepositoryImpl
import com.application.moviesapp.data.repository.AccountSetupRepository
import com.application.moviesapp.data.repository.AccountSetupRepositoryImpl
import com.application.moviesapp.data.repository.AuthRepository
import com.application.moviesapp.data.repository.AuthRepositoryImpl
import com.application.moviesapp.data.repository.FacebookRepositoryImpl
import com.application.moviesapp.data.repository.GoogleRepositoryImpl
import com.application.moviesapp.data.repository.LanguagePreferenceImpl
import com.application.moviesapp.data.repository.LanguagePreferenceRepository
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.data.repository.MoviesRepositoryImpl
import com.application.moviesapp.data.repository.NotificationPreferencePreferenceImpl
import com.application.moviesapp.data.repository.NotificationPreferenceRepository
import com.application.moviesapp.data.repository.PasswordResetRepository
import com.application.moviesapp.data.repository.PasswordResetRepositoryImpl
import com.application.moviesapp.data.repository.SettingsPreferenceImpl
import com.application.moviesapp.data.repository.SettingsPreferenceRepository
import com.application.moviesapp.data.repository.SignInEmailRepositoryImpl
import com.application.moviesapp.data.repository.SignUpEmailRepositoryImpl
import com.application.moviesapp.data.repository.UserPreferenceRepoImpl
import com.application.moviesapp.data.repository.UserPreferenceRepository
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
    abstract fun providesUserPreferencesRepositoryImpl(userPreferenceRepoImpl: UserPreferenceRepoImpl): UserPreferenceRepository

    //Google
    @Binds
    @Named("GoogleRepo")
    abstract fun providesGoogleRepoImpl(googleRepoImpl: GoogleRepositoryImpl): AuthRepository

    //Github
    @Binds
    @Named("GithubRepo")
    abstract fun providesGithubRepoImpl(authRepoImpl: AuthRepositoryImpl): AuthRepository

    //Facebook
    @Binds
    @Named("FacebookRepo")
    abstract fun providesFacebookRepoImpl(facebookRepoImpl: FacebookRepositoryImpl): AuthRepository

    //Email
    @Binds
    @Named("SignInEmailRepo")
    abstract fun providesSignInEmailRepoImpl(signInEmailRepoImpl: SignInEmailRepositoryImpl): AuthRepository

    @Binds
    @Named("SignUpEmailRepo")
    abstract fun providesSignUpEmailRepoImpl(signUpEmailRepoImpl: SignUpEmailRepositoryImpl): AuthRepository

    @Binds
    abstract fun providesYoutubeRepositoryImpl(youtubeRepositoryImpl: YoutubeRepositoryImpl): YoutubeRepository

    @Binds
    abstract fun providesSettingsRepositoryImpl(settingPreferenceRepoImpl: SettingsPreferenceImpl): SettingsPreferenceRepository

    @Binds
    abstract fun providesDownloaderRepositoryImpl(downloaderRepositoryImpl: DownloaderRepositoryImpl): DownloaderRepository

    @Binds
    abstract fun providesWorkManagerRepositoryImpl(workManagerRepositoryImpl: WorkManagerRepositoryImpl): WorkManagerRepository

    @Binds
    abstract fun providesAccountSetupRepositoryImpl(accountSetupRepositoryImpl: AccountSetupRepositoryImpl): AccountSetupRepository

    @Binds
    abstract fun providesResetPasswordRepositoryImpl(passwordResetRepositoryImpl: PasswordResetRepositoryImpl): PasswordResetRepository

    @Binds
    abstract fun providesLanguageRepositoryImpl(languagePreferenceImpl: LanguagePreferenceImpl): LanguagePreferenceRepository

    @Binds
    abstract fun providesNotificationRepositoryImpl(notificationPreferenceImpl: NotificationPreferencePreferenceImpl): NotificationPreferenceRepository
}