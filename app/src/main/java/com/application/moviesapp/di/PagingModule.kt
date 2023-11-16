package com.application.moviesapp.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.local.MoviesDatabase
import com.application.moviesapp.data.local.entity.MoviesEntity
import com.application.moviesapp.data.remote.MoviesPopularPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object PagingModule {

}