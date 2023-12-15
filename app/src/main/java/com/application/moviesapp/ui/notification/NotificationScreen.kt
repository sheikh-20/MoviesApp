package com.application.moviesapp.ui.notification

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun NotificationScreen(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        NotificationScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun NotificationDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        NotificationScreen()
    }
}