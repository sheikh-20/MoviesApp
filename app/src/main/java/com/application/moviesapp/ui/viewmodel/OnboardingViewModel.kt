package com.application.moviesapp.ui.viewmodel

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.AuthRepository
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.usecase.SignInGoogleUseCase
import com.application.moviesapp.domain.usecase.UserInfoUseCase
import com.application.moviesapp.ui.onboarding.OnboardingActivity
import com.application.moviesapp.ui.signin.SignInResult
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

sealed interface MovieGenreUiState {
    object Loading: MovieGenreUiState
    data class Success(val genreResponse: MovieGenreResponse): MovieGenreUiState
    object Failure: MovieGenreUiState
}
@HiltViewModel
class OnboardingViewModel @Inject constructor(private val moviesRepository: MoviesRepository,
                                              private val signInGoogleUseCase: SignInGoogleUseCase,
                                              private val authRepository: AuthRepository,
                                                private val userInfoUseCase: UserInfoUseCase
    ): ViewModel() {

    private companion object {
        const val TAG = "OnboardingViewModel"
    }

    private var _movieGenreUiState = MutableStateFlow<MovieGenreUiState>(MovieGenreUiState.Loading)
    val movieGenreUiState: StateFlow<MovieGenreUiState> = _movieGenreUiState

    private var _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private var _googleIntent = MutableSharedFlow<Resource<IntentSender>>()
    val googleIntent get() = _googleIntent.asSharedFlow()


    private var _googleResult = MutableSharedFlow<Resource<SignInResult>>()
    val googleResult get() = _googleResult.asSharedFlow()

    private var _facebookSignIn = MutableSharedFlow<Resource<AuthCredential>>()
    val facebookSignIn get() = _facebookSignIn.asSharedFlow()


    fun getMoviesGenreList() = viewModelScope.launch(Dispatchers.IO) {
        _movieGenreUiState.value = MovieGenreUiState.Loading

        try {
            val result = moviesRepository.getMoviesGenreList()
            _movieGenreUiState.value = MovieGenreUiState.Success(result)
            Timber.tag(TAG).d(result.toString())
        } catch (exception: IOException) {
            _movieGenreUiState.value = MovieGenreUiState.Failure
            Timber.tag(TAG).e(exception)
        }
    }

    private fun showLoading() = viewModelScope.launch {
        _loading.value = false
    }

    fun signInGoogle() = viewModelScope.launch {
        signInGoogleUseCase().collectLatest {
            _googleIntent.emit(it)
        }
    }

    fun signInIntent(intent: Intent) = viewModelScope.launch {
        authRepository.signInWithIntent(intent).collectLatest {
            _googleResult.emit(it)
        }
    }

    fun getUserInfo() = userInfoUseCase()

    val loginManager = LoginManager.getInstance()
    val callbackManager = CallbackManager.Factory.create()
    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onCancel() {
            TODO("Not yet implemented")
        }

        override fun onError(error: FacebookException) {
            viewModelScope.launch {
                _facebookSignIn.emit(Resource.Failure(error))
            }
        }

        override fun onSuccess(result: LoginResult) {
            val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
            Timber.tag(TAG).d(credential.toString())
            viewModelScope.launch {
                _facebookSignIn.emit(Resource.Success(credential))
            }
        }
    }

    init {
        showLoading()
        loginManager.registerCallback(callbackManager, facebookCallback)
    }
}