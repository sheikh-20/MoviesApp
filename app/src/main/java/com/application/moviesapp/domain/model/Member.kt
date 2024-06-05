package com.application.moviesapp.domain.model

import android.net.Uri
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName(value = "fullName")
    val fullName: String = "",

    @SerialName(value = "nickName")
    val nickName: String = "",

    @SerialName("email")
    val email: String = "",

    @SerialName("phoneNumber")
    val phoneNumber: String = "",

    @SerialName(value = "gender")
    val gender: String = ""
    )

data class MemberPhoto(
    val profilePhoto: String = ""
)
