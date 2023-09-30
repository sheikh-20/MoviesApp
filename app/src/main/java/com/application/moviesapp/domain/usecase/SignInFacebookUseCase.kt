package com.application.moviesapp.domain.usecase

import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.AuthRepo
import com.application.moviesapp.ui.viewmodel.OnboardingViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

interface SignInFacebookUseCase {
    operator fun invoke(): Flow<Resource<AuthResult>>
}

class GetSignInFacebookInteractor @Inject constructor(private val authRepo: AuthRepo): SignInFacebookUseCase {
    companion object {
        val loginManager = LoginManager.getInstance()
        val callbackManager = CallbackManager.Factory.create()
        private const val TAG = "GetSignInFacebookInteractor"
    }

    private var _token = MutableSharedFlow<String>()
    val token get() = _token.asSharedFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onCancel() {

        }

        override fun onError(error: FacebookException) {

        }

        override fun onSuccess(result: LoginResult) {
//            Timber.tag(TAG).d(result.accessToken.token)
            coroutineScope.launch {
                _token.emit(result.accessToken.token)
            }
        }
    }

    override fun invoke(): Flow<Resource<AuthResult>> = flow {
        token.collectLatest {
            Timber.tag(TAG).d(it)
            authRepo.signIn(token = it)
        }
    }

    init {
        loginManager.registerCallback(callbackManager, facebookCallback)
    }
}