package com.application.moviesapp.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefsModule {

    private const val PREFERENCE_NAME = "preference_name"

    private val Context.datastore by preferencesDataStore(
        name = PREFERENCE_NAME
    )

    @Provides
    @Singleton
    fun providesPrefsDatastore(@ApplicationContext context: Context) = context.datastore
}