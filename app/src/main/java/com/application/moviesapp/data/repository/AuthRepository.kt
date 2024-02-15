package com.application.moviesapp.data.repository

import android.app.Activity
import android.content.Context
import com.application.moviesapp.data.common.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

interface AuthRepository {
    fun signIn(activity: Activity? = null, token: String? = null, email: String? = null, password: String? = null): Flow<Resource<AuthResult>>
}

class AuthRepositoryImpl @Inject constructor(private val context: Context, private val auth: FirebaseAuth): AuthRepository {

    private companion object {
        const val TAG = "AuthRepoImpl"
    }

    override fun signIn(activity: Activity?, token: String?, email: String?, password: String?): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading)

        val provider = OAuthProvider.newBuilder("github.com")
        val result = auth.startActivityForSignInWithProvider(activity ?: throw Throwable(), provider.build()).await()
        emit(Resource.Success(result))
    }.catch {
        it.printStackTrace()
        emit(Resource.Failure(it))
    }
}

class FacebookRepositoryImpl @Inject constructor(private val context: Context, private val auth: FirebaseAuth): AuthRepository {

    private companion object {
        const val TAG = "FacebookRepoImpl"
    }

    override fun signIn(activity: Activity?, token: String?, email: String?, password: String?): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading)

        val result = auth.signInWithCredential(FacebookAuthProvider.getCredential(token  ?: throw  Throwable() )).await()

        Timber.tag(TAG).d(result.additionalUserInfo.toString())
        emit(Resource.Success(result))
    }.catch {
        Timber.tag(TAG).e(it)
        it.printStackTrace()
        emit(Resource.Failure(it))
    }
}

class GoogleRepositoryImpl @Inject constructor(private val context: Context,
                                               private val auth: FirebaseAuth): AuthRepository {
    private companion object {
        const val TAG = "GoogleRepoImpl"
    }

    override fun signIn(activity: Activity?, token: String?, email: String?, password: String?): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading)

        val result = auth.signInWithCredential(GoogleAuthProvider.getCredential(token ?: throw Throwable(), null)).await()

        Timber.tag(TAG).d(result.additionalUserInfo.toString())
        emit(Resource.Success(result))
    }.catch {
        Timber.tag(TAG).e(it)
        it.printStackTrace()
        emit(Resource.Failure(it))
    }

}

class SignInEmailRepositoryImpl @Inject constructor(private val context: Context,
                                                    private val auth: FirebaseAuth): AuthRepository {

    private companion object {
        const val TAG = "SignInEmailImpl"
    }

    override fun signIn(activity: Activity?, token: String?, email: String?, password: String?): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading)

        val result = auth.signInWithEmailAndPassword(email.toString(), password.toString()).await()

        Timber.tag(TAG).d(result.additionalUserInfo.toString())
        emit(Resource.Success(result))
    }.catch {
        Timber.tag(TAG).e(it)
        it.printStackTrace()
        emit(Resource.Failure(it))
    }
}

class SignUpEmailRepositoryImpl @Inject constructor(private val context: Context,
                                                    private val auth: FirebaseAuth): AuthRepository {

    private companion object {
        const val TAG = "SignUpEmailImpl"
    }

    override fun signIn(activity: Activity?, token: String?, email: String?, password: String?): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading)

        val result = auth.createUserWithEmailAndPassword(email.toString(), password.toString()).await()

        Timber.tag(TAG).d(result.additionalUserInfo.toString())
        emit(Resource.Success(result))
    }.catch {
        Timber.tag(TAG).e(it)
        it.printStackTrace()
        emit(Resource.Failure(it))
    }
}