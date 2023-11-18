package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

interface SignInFacebookUseCase {
    operator fun invoke(token: String): Flow<Resource<AuthResult>>
}

class GetSignInFacebookInteractor @Inject constructor(private val authRepository: AuthRepository): SignInFacebookUseCase {
    companion object {

        private const val TAG = "GetSignInFacebookInteractor"
    }

    override fun invoke(token: String): Flow<Resource<AuthResult>> {
        Timber.tag(TAG).d(token.toString())
        return authRepository.signIn(token = token)
    }
}