package com.application.moviesapp.ui.viewmodel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.UserPreferences
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.model.Member
import com.application.moviesapp.domain.model.MovieGenre
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.domain.usecase.AccountSetupUseCase
import com.application.moviesapp.domain.usecase.MovieGenresUseCase
import com.application.moviesapp.domain.usecase.PasswordResetUseCase
import com.application.moviesapp.domain.usecase.SignInEmailUseCase
import com.application.moviesapp.domain.usecase.SignInFacebookUseCase
import com.application.moviesapp.domain.usecase.SignInGithubUseCase
import com.application.moviesapp.domain.usecase.SignInGoogleUseCase
import com.application.moviesapp.domain.usecase.SignUpEmailUseCase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

data class OnboardUIState(val isEmailError: Boolean = false, val isPasswordError: Boolean = false)
@HiltViewModel
class OnboardingViewModel @Inject constructor(private val movieGenresUseCase: MovieGenresUseCase,
                                              private val signInGoogleUseCase: SignInGoogleUseCase,
                                              private val signInGithubUseCase: SignInGithubUseCase,
                                              private val signInFacebookUseCase: SignInFacebookUseCase,
                                              private val accountSetupUseCase: AccountSetupUseCase,
                                              private val signInEmailUseCase: SignInEmailUseCase,
                                              private val signUpEmailUseCase: SignUpEmailUseCase,
    private val passwordResetUseCase: PasswordResetUseCase
    ): ViewModel() {

    private companion object {
        const val TAG = "OnboardingViewModel"
    }

    private val auth = Firebase.auth

    private var _movieGenreUiState = MutableStateFlow<Resource<MovieGenre>>(Resource.Loading)
    val movieGenreUiState: StateFlow<Resource<MovieGenre>> = _movieGenreUiState

    private var _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading


    private var _socialSignIn = MutableSharedFlow<Resource<AuthResult>>()
    val socialSignIn get() = _socialSignIn.asSharedFlow()


    private var _profilePhotoUIState = MutableStateFlow<Resource<Uri>>(Resource.Loading)
    val profilePhotoUIState get() = _profilePhotoUIState.asStateFlow()

    private var _profileUIState = MutableStateFlow<Resource<Member>>(Resource.Loading)
    val profileUIState get() = _profileUIState.asStateFlow()

    var email by mutableStateOf("")
        private set

    fun onEmailChange(value: String) {
        email = value
    }


    private var _loginUIState = MutableStateFlow(OnboardUIState())
    val loginUIState: StateFlow<OnboardUIState> get() = _loginUIState

    private var _signupUIState = MutableStateFlow(OnboardUIState())
    val signupUIState: StateFlow<OnboardUIState> get() = _signupUIState

    fun getMoviesGenreList() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _movieGenreUiState.value = movieGenresUseCase()
        } catch (exception: IOException) {
            Timber.tag(TAG).e(exception)
        }
    }

    val readUserPreference = accountSetupUseCase.readUserPreference.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = UserPreferences.getDefaultInstance())

    private val movieGenreSelectedList = mutableSetOf<MoviesDetail.Genre>()

    fun updateMovieGenre(genre: MoviesDetail.Genre) {
        movieGenreSelectedList.add(genre)
    }

    fun saveMovieGenre() = viewModelScope.launch(Dispatchers.IO) {
        try {
            accountSetupUseCase.updateGenre(movieGenreSelectedList)
        } catch (exception: IOException) {
            Timber.tag(TAG).e(exception)
        }
    }

    fun updateProfile(fullName: String, nickName: String, email: String, phoneNumber: Long, gender: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
//            accountSetupUseCase.updateProfile(fullName, nickName, email, phoneNumber, gender)
            accountSetupUseCase.updateInfo(auth.currentUser?.uid ?: return@launch, Member(fullName, nickName, email, phoneNumber.toString(), gender))
        } catch (exception: IOException) {
            Timber.tag(TAG).e(exception)
        }
    }

    fun uploadProfilePhoto(uri: Uri) = viewModelScope.launch {
        try {
            accountSetupUseCase.uploadProfilePhoto(auth.currentUser?.uid ?: return@launch, uri).collectLatest {
                when (it) {
                    is Resource.Loading -> {  }
                    is Resource.Failure -> {  }
                    is Resource.Success -> {
                        getProfilePhoto()
                    }
                }
            }
        } catch (exception: IOException) {
            Timber.tag(TAG).e(exception)
        }
    }

    fun getProfilePhoto() = viewModelScope.launch {
        try {
            accountSetupUseCase.getPhoto(auth.currentUser?.uid ?: return@launch).collectLatest {
                _profilePhotoUIState.value = it
            }
        } catch (exception: IOException) {
            Timber.tag(TAG).e(exception)
        }
    }

    fun getProfile() = viewModelScope.launch {
        try {
            accountSetupUseCase.getUserDetail(auth.currentUser?.uid ?: return@launch).collectLatest {
                _profileUIState.value = it
            }
        } catch (exception: IOException) {
            Timber.tag(TAG).e(exception)
        }
    }

    private fun showLoading() = viewModelScope.launch {
        _loading.value = false
    }

    fun getUserInfo() = auth.currentUser

    fun signInGithub(activity: Activity) = viewModelScope.launch {
        signInGithubUseCase(activity).collectLatest {
            _socialSignIn.emit(it)
        }
    }

    fun signInFacebook(token: String) = viewModelScope.launch {
        Timber.tag(TAG).d("Facebook called!")
        signInFacebookUseCase(token).collectLatest {
            _socialSignIn.emit(it)
        }
    }

    fun signInGoogle(activity: Activity?, intent: Intent?) = viewModelScope.launch {
        signInGoogleUseCase(activity, intent).collectLatest {
            Timber.tag(TAG).d("Google called")
            _socialSignIn.emit(it)
        }
    }

    fun signInEmail(email: String?, password: String?) = viewModelScope.launch {
        if (email.isNullOrEmpty()) {
            _loginUIState.update {
                it.copy(isEmailError = true)
            }
        } else if (email.contains("@").not() || email.contains(".com").not()) {
            _loginUIState.update {
                it.copy(isEmailError = true)
            }
        } else if (password.isNullOrEmpty()){
            _loginUIState.update {
                it.copy(isPasswordError = true)
            }
        } else {
            _loginUIState.update {
                it.copy(isEmailError = false, isPasswordError = false)
            }
            signInEmailUseCase(email = email, password = password).collectLatest {
                Timber.tag(TAG).d("Email called")
                _socialSignIn.emit(it)
            }
        }
    }

    fun signUpEmail(email: String?, password: String?) = viewModelScope.launch {
        if (email.isNullOrEmpty()) {
            _signupUIState.update {
                it.copy(isEmailError = true)
            }
        } else if (email.contains("@").not() || email.contains(".com").not()) {
            _signupUIState.update {
                it.copy(isEmailError = true)
            }
        } else if (password.isNullOrEmpty()){
            _signupUIState.update {
                it.copy(isPasswordError = true)
            }
        } else {
            signUpEmailUseCase(email = email, password = password).collectLatest {
                Timber.tag(TAG).d("Email called")
                _socialSignIn.emit(it)
            }
        }
    }

    fun sendPasswordResetOtp() = viewModelScope.launch {
        passwordResetUseCase.sendOtp(email)
    }

    init {
        showLoading()
    }
}