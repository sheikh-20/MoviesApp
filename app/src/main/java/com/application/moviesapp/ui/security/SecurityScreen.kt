package com.application.moviesapp.ui.security

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun SecurityScreen(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SecurityLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        SecurityScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SecurityDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        SecurityScreen()
    }
}