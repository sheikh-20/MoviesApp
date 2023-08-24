package com.application.moviesapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.local.MoviesDatabase
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
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    Timber.tag(TAG).d("Refresh called")
                    1
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {

                    Timber.tag(TAG).d("Append called")

                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        Timber.tag(TAG).d("last item called")
                        1
                    } else {
                        Timber.tag(TAG).d("new item called")
                        state.pages.size + 1
                    }
                }
            }

            val apiResult = api.getPopularMoviesList(page = page)
            Timber.tag(TAG).d("$apiResult")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.moviesDao.clearAll()
                }

                val moviesEntities = apiResult.results?.map { it?.toMoviesEntity() } ?: listOf()
                database.moviesDao.upsertAll(moviesEntities)
            }

            MediatorResult.Success(endOfPaginationReached = true)

        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }
}