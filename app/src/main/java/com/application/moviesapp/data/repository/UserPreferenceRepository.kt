package com.application.moviesapp.data.repository

import androidx.datastore.core.DataStore
import com.application.moviesapp.UserPreferences
import com.application.moviesapp.domain.model.MoviesDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

interface UserPreferenceRepository {

    val userPreferenceFlow: Flow<UserPreferences>

    suspend fun updateMovieGenre(genre: Set<MoviesDetail.Genre>)

    suspend fun updateProfile(fullName: String, nickName: String, email: String, phoneNumber: Long, gender: String)
}

class UserPreferenceRepoImpl @Inject constructor(private val userPreference: DataStore<UserPreferences>): UserPreferenceRepository {
    override val userPreferenceFlow: Flow<UserPreferences>
        get() = userPreference.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(UserPreferences.getDefaultInstance())
                } else {
                    throw exception
                }
            }

    override suspend fun updateMovieGenre(genre: Set<MoviesDetail.Genre>) {
        userPreference.updateData { currentPreference ->

            currentPreference.toBuilder()
                .clearGenre()
                .addAllGenre(
                    genre.map {
                        UserPreferences.MovieGenre.newBuilder().setId(it.id ?: 0).setName(it.name ?: "").build()
                    }).build()
        }
    }

    override suspend fun updateProfile(
        fullName: String,
        nickName: String,
        email: String,
        phoneNumber: Long,
        gender: String
    ) {

        userPreference.updateData { currentPrefrence ->
             currentPrefrence.toBuilder()
                 .setName(fullName)
                 .setNickname(nickName)
                 .setEmail(email)
                 .setPhoneNumber(phoneNumber)
                 .build()
        }
    }
}