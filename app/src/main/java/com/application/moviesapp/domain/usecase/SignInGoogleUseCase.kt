package com.application.moviesapp.domain.usecase

import android.app.PendingIntent
import android.content.IntentSender
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SignInGoogleUseCase {
    operator fun invoke(): Flow<Resource<IntentSender>>
}

class SignInGoogleInteractor @Inject constructor(private val authRepository: AuthRepository): SignInGoogleUseCase {
    override fun invoke(): Flow<Resource<IntentSender>> = authRepository.signInWithGoogle()
}