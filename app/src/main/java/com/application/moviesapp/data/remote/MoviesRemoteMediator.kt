package com.application.moviesapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.local.MoviesDatabase
import com.application.moviesapp.data.local.entity.MovieRemoteKeyEntity
import com.application.moviesapp.data.local.entity.MoviesEntity
import com.application.moviesapp.data.mappers.toMoviesEntity
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator @Inject constructor(private val api: MoviesApi, private val database: MoviesDatabase):  RemoteMediator<Int, MoviesEntity>() {

    private companion object {
        const val TAG = "MoviesRemoteMediator"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MoviesEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    Timber.tag(TAG).d("Refresh called")
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    Timber.tag(TAG).d("Prepend called")

                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.previousPage ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevPage
                }
                LoadType.APPEND -> {

                    Timber.tag(TAG).d("Append called")

                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val apiResult = api.getPopularMoviesList(page = currentPage)
            val endOfPaginationReached = apiResult.results?.isEmpty() ?: true

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.moviesDao.clearAll()
                    database.remoteKeyDao.deleteAllRemoteKeys()
                }

                val moviesEntities = apiResult.results?.map { it?.toMoviesEntity() } ?: listOf()
                database.moviesDao.upsertAll(moviesEntities)

                val keys = apiResult.results?.map { moviesEntity ->
                    MovieRemoteKeyEntity(
                        id = moviesEntity?.id ?: 1,
                        previousPage = prevPage,
                        nextPage = nextPage
                    )
                }
                database.remoteKeyDao.upsertAll(remoteKeys = keys ?: listOf())
                database.moviesDao.upsertAll(movies = apiResult.results?.map { it?.toMoviesEntity() } ?: listOf())
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MoviesEntity>): MovieRemoteKeyEntity? {
        return state.anchorPosition?.let {  position ->
            state.closestItemToPosition(position)?.movieId?.let {  id ->
                database.remoteKeyDao.getRemoteKeys(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MoviesEntity>): MovieRemoteKeyEntity? {
        return state.pages.firstOrNull {  it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { moviesEntity ->
                database.remoteKeyDao.getRemoteKeys(moviesEntity.movieId ?: 1)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MoviesEntity>
    ): MovieRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { moviesEntity ->
                database.remoteKeyDao.getRemoteKeys(moviesEntity.movieId ?: 1)
            }
    }

}