package com.application.moviesapp.ui.onboarding.login

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.ui.home.HomeActivity
import com.application.moviesapp.ui.signin.SignInResult
import com.application.moviesapp.ui.theme.MoviesAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun LoginScreen(modifier: Modifier = Modifier,
                onSignInClick: () -> Unit = {},
                onSignupClick: () -> Unit = {},
                onGoogleSignInClick: () -> Unit = {},
                uiState: SharedFlow<Resource<IntentSender>>? = null,
                onSignInWithIntent: (Intent) -> Unit = {},
                resultUiState: SharedFlow<Resource<SignInResult>>? = null,
                onFacebookSignInClick: () -> Unit = {}
                ) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Timber.tag("LoginScreen").d(result.data?.data.toString())
                onSignInWithIntent(result.data ?: return@rememberLauncherForActivityResult)
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        resultUiState?.collectLatest {
            when (it) {
                is Resource.Loading -> {  }
                is Resource.Failure -> { }
                is Resource.Success -> {
                    (context as Activity).finish()
                    HomeActivity.startActivity((context as Activity))
                }
            }
        }
    }

   LaunchedEffect(key1 = Unit) {
       uiState?.collectLatest {
           when (it) {
               is Resource.Loading -> {
                   Toast.makeText(context, "Loading..", Toast.LENGTH_LONG).show()
               }
               is Resource.Failure -> {
                   Toast.makeText(context, "Failure..", Toast.LENGTH_LONG).show()
                   Timber.tag("Login").e(it.throwable)
               }
               is Resource.Success -> {
                   Toast.makeText(context, "Success..", Toast.LENGTH_LONG).show()
                   launcher.launch(IntentSenderRequest.Builder(it.data).build())
               }
           }
       }
   }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {

        Image(painter = painterResource(id = R.drawable.ic_login),
            contentDescription = null,
            modifier = modifier.size(200.dp))

        Text(text = stringResource(R.string.let_s_you_in),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.SemiBold)

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            LoginComponent(
                icon = R.drawable.ic_facebook,
                text = R.string.continue_with_facebook,
                onClick = onFacebookSignInClick)

            LoginComponent(
                icon = R.drawable.ic_google,
                text = R.string.continue_with_google,
                onClick = onGoogleSignInClick)

            LoginComponent(icon = R.drawable.ic_github, text = R.string.continue_with_github) {

            }
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.padding(horizontal = 16.dp)) {
            Divider(modifier = modifier.weight(1f), color = Color.LightGray)
            Text(text = stringResource(R.string.or))
            Divider(modifier = modifier.weight(1f), color = Color.LightGray)
        }

        Button(onClick = onSignInClick,
            modifier = modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.signin_with_password),
                modifier = modifier.padding(4.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.don_t_have_an_account))
            TextButton(onClick = onSignupClick) {
                Text(text = stringResource(R.string.sign_up),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LoginComponent(modifier: Modifier = Modifier, @DrawableRes icon: Int, @StringRes text: Int, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick,
        shape = RoundedCornerShape(30),
        border = BorderStroke(width = .5.dp, color =  Color.LightGray),
      ) {

        Row(modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Icon(painter = painterResource(id = icon),
                contentDescription = null,
                modifier = modifier.size(30.dp))

            Spacer(modifier = modifier.padding(horizontal = 8.dp))

            Text(text = stringResource(id = text),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        LoginScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        LoginScreen()
    }
}