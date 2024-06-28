package com.application.moviesapp.ui.onboarding.signup

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.usecase.SignInGoogleInteractor
import com.application.moviesapp.ui.accountsetup.AccountSetupActivity
import com.application.moviesapp.ui.home.HomeActivity
import com.application.moviesapp.ui.onboarding.OnboardingActivity
import com.application.moviesapp.ui.onboarding.component.EmailComponent
import com.application.moviesapp.ui.onboarding.component.PasswordComponent
import com.application.moviesapp.ui.onboarding.component.SocialLoginComponent
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.viewmodel.OnboardUIState
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupWithPasswordScreen(modifier: Modifier = Modifier,
                             onSignupClick: (String?, String?) -> Unit = { _, _ ->},
                             onGoogleSignInClick: (Activity?, Intent?) -> Unit = { _, _ ->},
                             onGithubSignInClick: () -> Unit = {},
                             onSocialSignIn: SharedFlow<Resource<AuthResult>>? = null,
                             onSignInClick: () -> Unit = { },
                             snackbarHostState: SnackbarHostState = SnackbarHostState(),
                             signupUIState: OnboardUIState = OnboardUIState(),
                             sendVerificationEmail: (() -> Unit, (Exception) -> Unit) -> Unit =  { _, _ -> }) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var isLoading by remember { mutableStateOf(false) }

    var isTermsConditions by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Timber.tag("LoginScreen").d("OK -> ${result.data.toString()}")

                onGoogleSignInClick(context as Activity, result.data)
            } else {
                Timber.tag("LoginScreen").e("ERROR")
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        onSocialSignIn?.collectLatest {
            when(it) {
                is Resource.Loading -> {
                     isLoading = true
                }
                is Resource.Failure -> {
                    isLoading = false

                    if (it.throwable is FirebaseAuthUserCollisionException) {
                        val result = snackbarHostState.showSnackbar(message = "Already created", actionLabel = "Try Login!")

                        when (result) {
                            SnackbarResult.ActionPerformed -> onSignInClick()
                            else -> { }
                        }
                    } else {
                        snackbarHostState.showSnackbar(message = "Failure!")
                        Timber.tag("Login").e(it.throwable)
                    }
                }
                is Resource.Success -> {
                    isLoading = false
                    Timber.tag("Login").d("Google Success")

                    if (it.data.user?.isEmailVerified == true) {
                        if (it.data.additionalUserInfo?.isNewUser == true) {
                            (context as Activity).finish()
                            AccountSetupActivity.startActivity(context as Activity)
                        } else {
                            (context as Activity).finish()
                            HomeActivity.startActivity((context as Activity))
                        }
                    } else {
                        sendVerificationEmail(
                            {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = "Check your inbox to verify email.")
                                }
                            },
                            {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = it.message.toString())
                                }
                            }
                        )
                    }
                }
            }
        }
    }


    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {

        Image(
            painter = painterResource(id = R.drawable.ic_movie),
            contentDescription = null,
            modifier = modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = stringResource(R.string.create_your_account),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Medium
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {


            EmailComponent(
                email = email,
                onEmailUpdate = { email = it },
                focusManager = focusManager,
                emailError = signupUIState.isEmailError)

            PasswordComponent(
                password = password,
                onPasswordUpdate = { password = it },
                focusManager = focusManager,
                passwordError = signupUIState.isPasswordError
            )


            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isTermsConditions, onCheckedChange = { isTermsConditions = !isTermsConditions })
                Text(text = "I agree to Mflix\'s terms & conditions", style = MaterialTheme.typography.labelLarge)
            }

            Button(onClick = { onSignupClick(email, password) },
                modifier = modifier
                    .shadow(
                        elevation = 4.dp,
                        ambientColor = MaterialTheme.colorScheme.outlineVariant,
                        spotColor = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(50)
                    )
                    .fillMaxWidth(),
                enabled = isTermsConditions && email.contains("@") && password.isNotEmpty(),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color.Red)) {

                if (isLoading) {
                    CircularProgressIndicator(modifier = modifier.size(30.dp), strokeWidth = 2.dp, trackColor = Color.White)
                } else {
                    Text(text = stringResource(id = R.string.sign_up), color = colorResource(id = R.color.white), modifier = modifier.padding(4.dp))
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Divider(modifier = modifier.weight(1f), color = Color.LightGray)
            Text(text = stringResource(R.string.or_continue_with))
            Divider(modifier = modifier.weight(1f), color = Color.LightGray)
        }

        Row(modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(align = Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            SocialLoginComponent(
                icon = R.drawable.ic_facebook,
                onClick = {
                    OnboardingActivity.loginManager.logInWithReadPermissions(context as ActivityResultRegistryOwner, OnboardingActivity.callbackManager, mutableListOf("email", "public_profile"))
                })

            SocialLoginComponent(
                icon = R.drawable.ic_google,
                onClick = {
                    coroutineScope.launch {
                        val result = SignInGoogleInteractor.signIn(context)
                        launcher.launch(IntentSenderRequest.Builder(result ?: return@launch).build())
                    }
                })

            SocialLoginComponent(
                icon = R.drawable.ic_github,
                onClick = onGithubSignInClick)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.already_have_an_account))
            TextButton(onClick = onSignInClick) {
                Text(text = stringResource(id = R.string.sign_in), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignupWithPasswordScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        SignupWithPasswordScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SignupWithPasswordScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        SignupWithPasswordScreen()
    }
}