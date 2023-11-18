package com.application.moviesapp.ui.signin

import com.google.firebase.auth.AdditionalUserInfo

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String?,
    val userName: String?,
    val profilePictureUrl: String?,
    val email: String?,
    val additionalUserInfo: AdditionalUserInfo? = null
)
