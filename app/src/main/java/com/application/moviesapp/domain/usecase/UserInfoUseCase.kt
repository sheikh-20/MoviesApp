package com.application.moviesapp.domain.usecase

import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.AuthRepository
import com.application.moviesapp.ui.signin.UserData
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface UserInfoUseCase {
    operator fun invoke(): UserData?
}

class GetUserInfoInteractor @Inject constructor(private val authRepository: AuthRepository): UserInfoUseCase {
    override fun invoke(): UserData? = authRepository.getUserInfo()
}