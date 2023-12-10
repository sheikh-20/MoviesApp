package com.application.moviesapp.data.repository

import com.application.moviesapp.domain.model.Member
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AccountSetupRepository {
    suspend operator fun invoke(userId: String, member: Member)
}

class AccountSetupRepositoryImpl @Inject constructor(private val database: FirebaseDatabase): AccountSetupRepository {
    override suspend fun invoke(userId: String, member: Member) {
        database.getReference("user").child(userId).setValue(member)
    }
}