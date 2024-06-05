package com.application.moviesapp.data.api.request

import android.net.Uri
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
