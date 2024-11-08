package com.application.moviesapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.local.MoviesDatabase
import com.application.moviesapp.data.local.entity.MovieNowPlayingEntity
import com.application.moviesapp.data.local.entity.MovieNowPlayingRemoteKeyEntity
import com.application.moviesapp.data.mappers.toMoviesEntity
import com.application.moviesapp.data.repository.MoviesRepository
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class MovieNowPlayingRemoteMediator @Inject constructor(private val moviesApi: MoviesApi,
                                                        private val database: MoviesDatabase): RemoteMediator<Int, MovieNowPlayingEntity>() {

    private companion object {
        const val TAG = "MoviesNowPlayingRemoteMediator"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieNowPlayingEntity>
    ): MediatorResult {


        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                //New Query so clear the DB
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextPage?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.previousPage
                prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextPage
                nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {

            val apiResult = moviesApi.getNowPlayingMovieList(page = page)
            val movies = if (apiResult.isSuccessful) {
                apiResult.body()?.results
            } else if (apiResult.code() == 400 || apiResult.code() == 401 || apiResult.code() == 403) {
                throw IOException()
            } else {
                throw IOException()
            }


            val endOfPaginationReached = movies?.isEmpty() ?: false

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.movieNowPlayingDao.clearAll()
                    database.movieNowPlayingRemoteKeyDao.deleteAllRemoteKeys()
                }

                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1

//                val moviesEntities = movies?.map { it?.toMoviesEntity() } ?: listOf()
//                database.movieNowPlayingDao.upsertAll(moviesEntities)

                val keys = movies?.map { moviesEntity ->
                    MovieNowPlayingRemoteKeyEntity(
                        movieId = moviesEntity?.id!!,
                        previousPage = prevKey,
                        nextPage = nextKey,
                        currentPage = page
                    )
                }
                database.movieNowPlayingRemoteKeyDao.upsertAll(remoteKeys = keys ?: listOf())
                database.movieNowPlayingDao.upsertAll(movies = movies?.map { it!!.toMoviesEntity() } ?: listOf())
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieNowPlayingEntity>): MovieNowPlayingRemoteKeyEntity? {
        return state.anchorPosition?.let {  position ->
            state.closestItemToPosition(position)?.movieId?.let {  id ->
                database.movieNowPlayingRemoteKeyDao.getRemoteKeys(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieNowPlayingEntity>): MovieNowPlayingRemoteKeyEntity? {
        return state.pages.firstOrNull {  it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { moviesEntity ->
                database.movieNowPlayingRemoteKeyDao.getRemoteKeys(moviesEntity.movieId ?: 0)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieNowPlayingEntity>
    ): MovieNowPlayingRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { moviesEntity ->
                database.movieNowPlayingRemoteKeyDao.getRemoteKeys(moviesEntity.movieId ?: 0)
            }
    }

}