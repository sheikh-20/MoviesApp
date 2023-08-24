package com.application.moviesapp.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun NotificationScreen(modifier: Modifier = Modifier) {
    Text(text = "Notification Screen")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        NotificationScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun NotificationScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        NotificationScreen()
    }
}