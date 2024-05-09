package com.application.moviesapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.api.response.TvSeriesDiscoverDto
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class TvSeriesDiscoverPagingSource @Inject constructor(private val moviesApi: MoviesApi,
                                                     private val genre: String,
                                                     private val sortBy: String,
                                                     private val includeAdult: Boolean):  PagingSource<Int, TvSeriesDiscoverDto.Result>() {

    private companion object {
        const val TAG = "TvSeriesRemoteMediator"
    }

    override fun getRefreshKey(state: PagingState<Int, TvSeriesDiscoverDto.Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvSeriesDiscoverDto.Result> {
        return try {
            val page = params.key ?: 1

            val apiResult = moviesApi.getDiscoverTvSeriesList(page = page, genres = genre, sortBy = sortBy, includeAdult = includeAdult)
            val movies = if (apiResult.isSuccessful) {
                apiResult.body()
            } else if (apiResult.code() == 400 || apiResult.code() == 401 || apiResult.code() == 403) {
                throw Throwable()
            } else {
                throw Throwable()
            }

            LoadResult.Page(
                data = movies?.results?.map { it ?: TvSeriesDiscoverDto.Result(null, null, null, null, null, null, null, null, null, null, null, null, null, null)} ?: listOf(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (movies?.results?.isEmpty() == true) null else page.plus(1),
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }

}