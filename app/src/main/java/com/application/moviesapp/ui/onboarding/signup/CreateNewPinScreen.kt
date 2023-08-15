package com.application.moviesapp.ui.onboarding.signup

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun CreateNewPinScreen(modifier: Modifier = Modifier, onContinueClick: () -> Unit = {}) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        
        Spacer(modifier = modifier.weight(1f))

        Button(onClick = onContinueClick, modifier = modifier.fillMaxWidth()) {
            Text(text = "Continue")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateNewPinScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        CreateNewPinScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CreateNewPinScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        CreateNewPinScreen()
    }
}