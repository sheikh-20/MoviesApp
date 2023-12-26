package com.application.moviesapp.ui.onboarding.login

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginWithPasswordScreen(modifier: Modifier = Modifier,
                            onSignupClick: () -> Unit = {},
                            onGoogleSignInClick: (Activity?, Intent?) -> Unit = { _, _ ->},
                            onGithubSignInClick: () -> Unit = {},
                            onSocialSignIn: SharedFlow<Resource<AuthResult>>? = null,
                            onSignInClick: (String?, String?) -> Unit = { _, _ ->},
                            snackbarHostState: SnackbarHostState = SnackbarHostState(),
                            onForgotPasswordClick: () -> Unit = { },
                            email: String = "",
                            onEmailChange: (String) -> Unit = { _ -> },
                            loginUIState: OnboardUIState = OnboardUIState()) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var password by remember { mutableStateOf("") }

    var isLoading by remember {
        mutableStateOf(false)
    }

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
                    if (it.throwable is FirebaseAuthInvalidUserException) {
                        snackbarHostState.showSnackbar(message = "Email does not exists, Try signup!")
                    } else {
                        snackbarHostState.showSnackbar(message = "Failure!")
                        Timber.tag("Login").e(it.throwable)
                    }
                }
                is Resource.Success -> {
                    isLoading = false
                    Timber.tag("Login").d("Google Success")

                    if (it.data.additionalUserInfo?.isNewUser == true) {
                        (context as Activity).finish()
                        AccountSetupActivity.startActivity(context as Activity)
                    } else {
                        (context as Activity).finish()
                        HomeActivity.startActivity((context as Activity))
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
            text = "Login To Your Account",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Medium
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            EmailComponent(
                email = email,
                onEmailUpdate = onEmailChange,
                focusManager = focusManager,
                emailError = loginUIState.isEmailError)

            PasswordComponent(
                password = password, 
                onPasswordUpdate = { password = it },
                focusManager = focusManager,
                passwordError = loginUIState.isPasswordError
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = false, onCheckedChange = {})
                Text(text = "Remember me", style = MaterialTheme.typography.labelLarge)
            }

            Button(onClick = { onSignInClick(email, password) },
                modifier = modifier
                    .shadow(
                        elevation = 4.dp,
                        ambientColor = MaterialTheme.colorScheme.outlineVariant,
                        spotColor = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(50)
                    )
                    .fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color.Red)) {

                if (isLoading) {
                    CircularProgressIndicator(modifier = modifier.size(30.dp), strokeWidth = 2.dp, trackColor = Color.White)
                } else {
                    Text(text = stringResource(id = R.string.signin), color = colorResource(id = R.color.white), modifier = modifier.padding(4.dp))
                }
            }
        }

        TextButton(onClick = onForgotPasswordClick) {
            Text(text = "Forgot the password?", fontWeight = FontWeight.Bold)
        }

        Row(modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(align = Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Divider(modifier = modifier.weight(1f), color = Color.LightGray)
            Text(text = "or continue with")
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
            Text(text = "Don't have an account?")
            TextButton(onClick = onSignupClick) {
                Text(text = "Sign up", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginWithPasswordScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        LoginWithPasswordScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LoginWithPasswordScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        LoginWithPasswordScreen()
    }
}