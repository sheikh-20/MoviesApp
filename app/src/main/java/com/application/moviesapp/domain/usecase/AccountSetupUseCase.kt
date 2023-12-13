package com.application.moviesapp.domain.usecase

import android.net.Uri
import com.application.moviesapp.UserPreferences
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.AccountSetupRepository
import com.application.moviesapp.data.repository.AccountSetupRepositoryImpl
import com.application.moviesapp.data.repository.UserPreferenceRepository
import com.application.moviesapp.domain.model.Member
import com.application.moviesapp.domain.model.MemberPhoto
import com.application.moviesapp.domain.model.MoviesDetail
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

interface AccountSetupUseCase {

    val readUserPreference: Flow<UserPreferences>

    suspend fun updateGenre(genre: Set<MoviesDetail.Genre>)

    suspend fun updateProfile(fullName: String, nickName: String, email: String, phoneNumber: Long, gender: String)

    suspend fun updateInfo(userId: String, member: Member)

    fun uploadProfilePhoto(userId: String, uri: Uri): Flow<Resource<UploadTask.TaskSnapshot>>

    fun getPhoto(userId: String): Flow<Resource<Uri>>
}

class GetAccountSetupInteractor @Inject constructor(private val repository: UserPreferenceRepository,
                                                    private val accountSetupRepository: AccountSetupRepository): AccountSetupUseCase {

                                                        private companion object {
                                                            const val TAG = "GetAccountSetupInteractor"
                                                        }

    override val readUserPreference: Flow<UserPreferences>
        get() = repository.userPreferenceFlow

    override suspend fun updateGenre(genre: Set<MoviesDetail.Genre>) {
        repository.updateMovieGenre(genre = genre)
    }

    override suspend fun updateProfile(
        fullName: String,
        nickName: String,
        email: String,
        phoneNumber: Long,
        gender: String
    ) {
        repository.updateProfile(fullName, nickName, email, phoneNumber, gender)
    }

    override suspend fun updateInfo(userId: String, member: Member) = accountSetupRepository.uploadUserData(userId, member)

    override fun uploadProfilePhoto(userId: String, uri: Uri): Flow<Resource<UploadTask.TaskSnapshot>> = accountSetupRepository.uploadPhoto(userId, uri)

    override fun getPhoto(userId: String): Flow<Resource<Uri>> = accountSetupRepository.getPhoto(userId)
}