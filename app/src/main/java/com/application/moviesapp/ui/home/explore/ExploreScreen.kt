package com.application.moviesapp.ui.home.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun ExploreScreen(modifier: Modifier = Modifier) {
    Column {
        Text(text = "Explore Screen")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ExploreScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        ExploreScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ExploreScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        ExploreScreen()
    }
}