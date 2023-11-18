package com.application.moviesapp.ui.onboarding.signup

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.ui.home.HomeActivity
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun SetYourFingerprintScreen(modifier: Modifier = Modifier, onContinueClick: () -> Unit = {}) {

    val context = LocalContext.current

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Spacer(modifier = modifier.weight(1f))

        Row(modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = { /*TODO*/ }, modifier = modifier.weight(1f)) {
                Text(text = "Skip")
            }
            Button(onClick = onContinueClick, modifier = modifier.weight(1f)) {
                Text(text = "Continue")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SetYourFingerprintScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        SetYourFingerprintScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SetYourFingerprintDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        SetYourFingerprintScreen()
    }
}