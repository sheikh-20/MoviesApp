package com.application.moviesapp.data.repository

import com.application.moviesapp.data.common.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

interface PasswordResetRepository {
    suspend fun sendOtp(email: String)
}

class PasswordResetRepositoryImpl @Inject constructor(private val auth: FirebaseAuth): PasswordResetRepository {

    private companion object {
        const val TAG = "PasswordResetRepositoryImpl"
    }

    override suspend fun sendOtp(email: String) {
        auth.sendPasswordResetEmail(email)
    }

}