package com.application.moviesapp.ui.detail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.domain.Movies
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun DetailScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .wrapContentSize(align = Alignment.Center)) {
        Text(text = "Detail Screen")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        DetailScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DetailScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        DetailScreen()
    }
}