package com.application.moviesapp.ui.onboarding.signup

import android.content.res.Configuration
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.R
import com.application.moviesapp.ui.onboarding.component.SocialLoginComponent
import com.application.moviesapp.ui.theme.MoviesAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupWithPasswordScreen(modifier: Modifier = Modifier, onSigninClick: () -> Unit = {}, onSignupClick: () -> Unit = {}) {
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
            text = "Create Your Account",
            style = MaterialTheme.typography.headlineLarge
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Email") },
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Email, contentDescription = null)
                },
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Password") },
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Lock, contentDescription = null)
                },
                modifier = modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(imageVector = Icons.Outlined.VisibilityOff, contentDescription = null)
                },
                shape = RoundedCornerShape(30)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = false, onCheckedChange = {})
                Text(text = "Remember me", style = MaterialTheme.typography.labelLarge)
            }

            Button(onClick = onSignupClick,
                modifier = modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color.Red)) {
                Text(text = stringResource(id = R.string.sign_up), color = colorResource(id = R.color.white), modifier = modifier.padding(4.dp))
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically,
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
            SocialLoginComponent(icon = R.drawable.ic_facebook) {

            }
            SocialLoginComponent(icon = R.drawable.ic_google) {

            }
            SocialLoginComponent(icon = R.drawable.ic_github) {

            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.already_have_an_account))
            TextButton(onClick = onSigninClick) {
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