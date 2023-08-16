package com.application.moviesapp.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun TopMoviesScreen(modifier: Modifier = Modifier) {
    Text(text = "Top Movies Screen")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TopMoviesScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        TopMoviesScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TopMoviesScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        TopMoviesScreen()
    }
}

