package com.application.moviesapp.di

import com.chaquo.python.Python
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PythonModule {

    @Provides
    @Singleton
    fun providesPythonInstance() = Python.getInstance()
}