package com.application.moviesapp.data.repository

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.ui.signin.SignInResult
import com.application.moviesapp.ui.signin.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.CancellationException
import javax.inject.Inject

interface AuthRepository {
    fun getUserInfo(): UserData?
    fun signInWithGoogle(): Flow<Resource<IntentSender>>
    fun signInWithIntent(intent: Intent): Flow<Resource<SignInResult>>

    suspend fun signOut()
}

class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth,
                                             private val context: Context,
                                             private val oneTapClient: SignInClient): AuthRepository {

    private companion object {
        const val TAG = "AuthRepositoryImpl"
    }

    override fun signInWithGoogle(): Flow<Resource<IntentSender>> = flow {
        emit(Resource.Loading)

        val result = oneTapClient.beginSignIn(buildSignInRequest()).await()

        emit(Resource.Success(result.pendingIntent.intentSender))
    }.catch {
        it.printStackTrace()
        emit(Resource.Failure(it))
    }

    override fun signInWithIntent(intent: Intent): Flow<Resource<SignInResult>> = flow {
        emit(Resource.Loading)

        Timber.tag(TAG).d("AuthRepo called!")

        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)


        val user = auth.signInWithCredential(googleCredentials).await().user

        Timber.tag(TAG).d("User name -> ${user?.displayName}")

        val result =  SignInResult(
            data = user?.run {
                UserData(
                    userId = uid,
                    userName = displayName,
                    profilePictureUrl = photoUrl.toString()
                ) },
            errorMessage = null
        )
        emit(Resource.Success(result))
    }.catch {
        it.printStackTrace()
        emit(Resource.Failure(it))
    }

    override fun getUserInfo(): UserData? = auth.currentUser?.run {
        UserData(userId = uid, userName = displayName, profilePictureUrl = photoUrl.toString())
    }

    override suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (exception: Exception) {
            exception.printStackTrace()
            if (exception is CancellationException) throw exception
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}