package com.application.moviesapp.domain.usecase

import android.app.Activity
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.AuthRepo
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SignInGithubUseCase {
    operator fun invoke(activity: Activity?): Flow<Resource<AuthResult>>
}

class SignInGithubInteractor @Inject constructor(private val authRepo: AuthRepo): SignInGithubUseCase {
    override operator fun invoke(activity: Activity?): Flow<Resource<AuthResult>> = authRepo.signIn(activity = activity)
}