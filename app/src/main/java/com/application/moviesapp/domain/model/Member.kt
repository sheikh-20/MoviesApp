package com.application.moviesapp.domain.model

import android.net.Uri

data class Member(
    val fullName: String = "",
    val nickName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val gender: String = ""
    )

data class MemberPhoto(
    val profilePhoto: String = ""
)
