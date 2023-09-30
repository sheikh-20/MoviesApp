package com.application.moviesapp.ui.play

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun PlayScreen(modifier: Modifier = Modifier) {
    Text(text = "Play Screen")
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PlayScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        PlayScreen()
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PlayScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        PlayScreen()
    }
}