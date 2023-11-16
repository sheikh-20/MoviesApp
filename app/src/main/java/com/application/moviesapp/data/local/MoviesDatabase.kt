package com.application.moviesapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.application.moviesapp.data.local.dao.MovieDownloadDao
import com.application.moviesapp.data.local.dao.MoviesDao
import com.application.moviesapp.data.local.dao.MovieNowPlayingDao
import com.application.moviesapp.data.local.dao.MoviesUpcomingDao
import com.application.moviesapp.data.local.dao.MovieNowPlayingRemoteKeyDao
import com.application.moviesapp.data.local.dao.RemoteKeyDao
import com.application.moviesapp.data.local.dao.UpcomingRemoteKeyDao
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.data.local.entity.MovieNowPlayingEntity
import com.application.moviesapp.data.local.entity.MovieNowPlayingRemoteKeyEntity
import com.application.moviesapp.data.local.entity.MovieRemoteKeyEntity
import com.application.moviesapp.data.local.entity.MovieUpcomingEntity
import com.application.moviesapp.data.local.entity.MovieUpcomingRemoteKeyEntity
import com.application.moviesapp.data.local.entity.MoviesEntity

@Database(
    entities = [
        MoviesEntity::class,
        MovieRemoteKeyEntity::class,
        MovieNowPlayingEntity::class,
        MovieNowPlayingRemoteKeyEntity::class,
        MovieUpcomingEntity::class,
        MovieUpcomingRemoteKeyEntity::class,
        MovieDownloadEntity::class
               ],
    version = 18,
    exportSchema = false)
abstract class MoviesDatabase: RoomDatabase() {

    abstract val moviesDao: MoviesDao
    abstract val remoteKeyDao: RemoteKeyDao
    abstract val movieNowPlayingDao: MovieNowPlayingDao
    abstract val movieNowPlayingRemoteKeyDao: MovieNowPlayingRemoteKeyDao
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