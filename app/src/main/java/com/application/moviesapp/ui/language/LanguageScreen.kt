package com.application.moviesapp.ui.language

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun LanguageScreen(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LanguageLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        LanguageScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LanguageDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        LanguageScreen()
    }
}