package com.application.moviesapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.application.moviesapp.data.local.dao.MovieDownloadDao
import com.application.moviesapp.data.local.dao.MoviesDao
import com.application.moviesapp.data.local.dao.MoviesNewReleaseDao
import com.application.moviesapp.data.local.dao.MoviesUpcomingDao
import com.application.moviesapp.data.local.dao.NewReleaseRemoteKeyDao
import com.application.moviesapp.data.local.dao.RemoteKeyDao
import com.application.moviesapp.data.local.dao.UpcomingRemoteKeyDao
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.data.local.entity.MovieNewReleaseEntity
import com.application.moviesapp.data.local.entity.MovieNewReleaseRemoteKeyEntity
import com.application.moviesapp.data.local.entity.MovieRemoteKeyEntity
import com.application.moviesapp.data.local.entity.MovieUpcomingEntity
import com.application.moviesapp.data.local.entity.MovieUpcomingRemoteKeyEntity
import com.application.moviesapp.data.local.entity.MoviesEntity

@Database(
    entities = [
        MoviesEntity::class,
        MovieRemoteKeyEntity::class,
        MovieNewReleaseEntity::class,
        MovieNewReleaseRemoteKeyEntity::class,
        MovieUpcomingEntity::class,
        MovieUpcomingRemoteKeyEntity::class,
        MovieDownloadEntity::class
               ],
    version = 7,
    exportSchema = false)
abstract class MoviesDatabase: RoomDatabase() {

    abstract val moviesDao: MoviesDao
    abstract val remoteKeyDao: RemoteKeyDao
    abstract val movieNewReleaseDao: MoviesNewReleaseDao
    abstract val movieNewReleaseRemoteKeyDao: NewReleaseRemoteKeyDao
    abstract val moviesUpcomingDao: MoviesUpcomingDao
    abstract val moviesUpcomingRemoteKeyEntity: UpcomingRemoteKeyDao
    abstract val movieDownloadDao: MovieDownloadDao

    companion object {
        @Volatile
        private var Instance: MoviesDatabase? = null

        fun getDatabase(context: Context): MoviesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MoviesDatabase::class.java, "movies_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}