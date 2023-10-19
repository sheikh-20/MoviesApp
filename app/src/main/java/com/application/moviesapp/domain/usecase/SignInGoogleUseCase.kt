package com.application.moviesapp.domain.usecase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.AuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.concurrent.CancellationException
import javax.inject.Inject

interface SignInGoogleUseCase {
    operator fun invoke(activity: Activity?, intent: Intent?): Flow<Resource<AuthResult>>
}

class SignInGoogleInteractor @Inject constructor(private val repository: AuthRepository,
                                                 private val oneTapClient: SignInClient): SignInGoogleUseCase {

    companion object {
        private const val TAG = "SignInGoogleUse"

        suspend fun signIn(context: Context): IntentSender? {

            Timber.tag(TAG).d("Google Domain Clicked!")

            val result = try {
                Identity.getSignInClient(context).beginSignIn(buildSignInRequest(context)).await()
            } catch (exception: Exception) {
                exception.printStackTrace()
                if (exception is CancellationException) throw exception
                null
            }

            return result?.pendingIntent?.intentSender
        }

        private fun buildSignInRequest(context: Context): BeginSignInRequest {
            return BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(context.getString(R.string.web_client_id))
                        .build())
                .setAutoSelectEnabled(true)
                .build()
        }
    }

    override fun invoke(activity: Activity?, intent: Intent?): Flow<Resource<AuthResult>> {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken

        return repository.signIn(token = googleIdToken)
    }
}