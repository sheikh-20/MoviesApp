package com.application.moviesapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import com.application.moviesapp.UserPreferences
import com.application.moviesapp.data.local.proto.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProtoModule {

    private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

    // Build the DataStore
    private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = UserPreferencesSerializer,
    )

    @Provides
    @Singleton
    fun providesProtoDatastore(@ApplicationContext context: Context) = context.userPreferencesStore
}