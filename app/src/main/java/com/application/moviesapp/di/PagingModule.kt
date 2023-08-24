package com.application.moviesapp.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.local.MoviesDatabase
import com.application.moviesapp.data.local.entity.MoviesEntity
import com.application.moviesapp.data.remote.MoviesRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object PagingModule {

    @Provides
    @Singleton
    fun providesMoviesPaging(moviesApi: MoviesApi, moviesDatabase: MoviesDatabase): Pager<Int, MoviesEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MoviesRemoteMediator(moviesApi, moviesDatabase),
            pagingSourceFactory = {
                moviesDatabase.moviesDao.pagingSource()
            }
        )
    }
}