package com.application.moviesapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.api.response.MovieSearchDto
import com.application.moviesapp.data.api.response.TvSeriesSearchDto
import timber.log.Timber

class TvSeriesSearchPagingSource(private val moviesApi: MoviesApi, private val search: String): PagingSource<Int, TvSeriesSearchDto.Result>() {

    private companion object {
        const val TAG = "TvSeriesSearchPagingSource"
    }

    override fun getRefreshKey(state: PagingState<Int, TvSeriesSearchDto.Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvSeriesSearchDto.Result> {
        return try {
            val page = params.key ?: 1

            val apiResult = moviesApi.getTvSeriesBySearch(query = search, page = page)
            val movies = if (apiResult.isSuccessful) {
                apiResult.body()
            } else if (apiResult.code() == 400 || apiResult.code() == 401 || apiResult.code() == 403) {
                throw Throwable()
            } else {
                throw Throwable()
            }

            LoadResult.Page(
                data = movies?.results?.map { it ?: TvSeriesSearchDto.Result(null, null, null, null, null, null, null, null, null, null, null, null, null, null)} ?: listOf(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (movies?.results?.isEmpty() == true) null else page.plus(1),
            )
        } catch (throwable: Throwable) {
            Timber.tag(TAG).e(throwable)
            LoadResult.Error(throwable)
        }
    }
}