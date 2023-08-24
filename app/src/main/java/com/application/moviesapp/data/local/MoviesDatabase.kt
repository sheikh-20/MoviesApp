package com.application.moviesapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.application.moviesapp.data.local.dao.MoviesDao
import com.application.moviesapp.data.local.dao.RemoteKeyDao
import com.application.moviesapp.data.local.entity.MovieRemoteKeyEntity
import com.application.moviesapp.data.local.entity.MoviesEntity

@Database(entities = [MoviesEntity::class, MovieRemoteKeyEntity::class], version = 1, exportSchema = false)
abstract class MoviesDatabase: RoomDatabase() {

    abstract val moviesDao: MoviesDao
    abstract val remoteKeyDao: RemoteKeyDao

    companion object {
        @Volatile
        private var Instance: MoviesDatabase? = null

        fun getDatabase(context: Context): MoviesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MoviesDatabase::class.java, "movies_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}