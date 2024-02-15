package com.application.moviesapp.di

import androidx.paging.ExperimentalPagingApi
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object PagingModule {

}