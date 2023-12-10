package com.application.moviesapp.domain.usecase

import com.application.moviesapp.UserPreferences
import com.application.moviesapp.data.repository.AccountSetupRepository
import com.application.moviesapp.data.repository.UserPreferenceRepository
import com.application.moviesapp.domain.model.Member
import com.application.moviesapp.domain.model.MoviesDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AccountSetupUseCase {

    val readUserPreference: Flow<UserPreferences>

    suspend fun updateGenre(genre: Set<MoviesDetail.Genre>)

    suspend fun updateProfile(fullName: String, nickName: String, email: String, phoneNumber: Long, gender: String)

    suspend fun updateInfo(userId: String, member: Member)
}

class GetAccountSetupInteractor @Inject constructor(private val repository: UserPreferenceRepository, private val accountSetupRepository: AccountSetupRepository): AccountSetupUseCase {

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

    override suspend fun updateInfo(userId: String, member: Member) = accountSetupRepository(userId, member)
}