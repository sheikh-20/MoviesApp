package com.application.moviesapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.api.response.MovieNowPlayingDto
import com.application.moviesapp.data.local.entity.MovieRemoteKeyEntity
import com.application.moviesapp.data.local.entity.MoviesEntity
import com.application.moviesapp.data.mappers.toMoviesEntity
import com.application.moviesapp.domain.model.MoviesPopular
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MoviesPopularPagingSource @Inject constructor(private val moviesApi: MoviesApi):  PagingSource<Int, MoviesPopularDto.Result>() {

    private companion object {
        const val TAG = "MoviesRemoteMediator"
    }

    override fun getRefreshKey(state: PagingState<Int, MoviesPopularDto.Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesPopularDto.Result> {
        return try {
            val page = params.key ?: 1

            val apiResult = moviesApi.getPopularMoviesList(page = page)
            val movies = if (apiResult.isSuccessful) {
                apiResult.body()
            } else if (apiResult.code() == 400 || apiResult.code() == 401 || apiResult.code() == 403) {
                throw Throwable()
            } else {
                throw Throwable()
            }

            LoadResult.Page(
                data = movies?.results?.map { it ?: MoviesPopularDto.Result(null, null, null, null, null, null, null, null, null, null, null, null, null, null)} ?: listOf(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (movies?.results?.isEmpty() == true) null else page.plus(1),
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }

}