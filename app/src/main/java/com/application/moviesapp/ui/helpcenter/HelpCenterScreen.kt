package com.application.moviesapp.ui.helpcenter

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun HelpCenterScreen() {
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HelpCenterLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        HelpCenterScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun HelpCenterDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        HelpCenterScreen()
    }
}