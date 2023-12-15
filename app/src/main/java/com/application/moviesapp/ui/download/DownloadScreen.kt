package com.application.moviesapp.ui.download

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
private fun DownloadScreen(modifier: Modifier = Modifier) {
    Column {

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DownloadLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        DownloadScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DownloadDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        DownloadScreen()
    }
}