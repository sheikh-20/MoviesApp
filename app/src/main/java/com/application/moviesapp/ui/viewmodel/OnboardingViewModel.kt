package com.application.moviesapp.ui.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.moviesapp.UserPreferences
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.repository.AuthRepository
import com.application.moviesapp.data.repository.MoviesRepository
import com.application.moviesapp.domain.model.Member
import com.application.moviesapp.domain.model.MovieGenre
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.domain.usecase.AccountSetupUseCase
import com.application.moviesapp.domain.usecase.MovieGenresUseCase
import com.application.moviesapp.domain.usecase.SignInEmailUseCase
import com.application.moviesapp.domain.usecase.SignInFacebookUseCase
import com.application.moviesapp.domain.usecase.SignInGithubUseCase
import com.application.moviesapp.domain.usecase.SignInGoogleUseCase
import com.application.moviesapp.domain.usecase.SignUpEmailUseCase
import com.application.moviesapp.domain.usecase.UserInfoUseCase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val movieGenresUseCase: MovieGenresUseCase,
                                              private val signInGoogleUseCase: SignInGoogleUseCase,
                                              private val signInGithubUseCase: SignInGithubUseCase,
                                              private val signInFacebookUseCase: SignInFacebookUseCase,
                                              private val accountSetupUseCase: AccountSetupUseCase,
                                              private val signInEmailUseCase: SignInEmailUseCase,
                                              private val signUpEmailUseCase: SignUpEmailUseCase
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
        signInEmailUseCase(email = email, password = password).collectLatest {
            Timber.tag(TAG).d("Email called")
            _socialSignIn.emit(it)
        }
    }

    fun signUpEmail(email: String?, password: String?) = viewModelScope.launch {
        signUpEmailUseCase(email = email, password = password).collectLatest {
            Timber.tag(TAG).d("Email called")
            _socialSignIn.emit(it)
        }
    }

    init {
        showLoading()
    }
}