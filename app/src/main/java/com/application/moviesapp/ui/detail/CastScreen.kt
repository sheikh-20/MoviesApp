package com.application.moviesapp.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun CastScreen(modifier: Modifier = Modifier, paddingValues: PaddingValues = PaddingValues()) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = paddingValues.calculateTopPadding(), start = 16.dp, end = 16.dp)) {
        Text(text = "Cast screen")
    }
}

@Preview(showBackground = true , showSystemUi = true)
@Composable
private fun CastScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        CastScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CastScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        CastScreen()
    }
}