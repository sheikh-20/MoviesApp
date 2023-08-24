package com.application.moviesapp.di

import android.content.Context
import com.application.moviesapp.data.local.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesMoviesDatabase(@ApplicationContext context: Context) = MoviesDatabase.getDatabase(context)

}