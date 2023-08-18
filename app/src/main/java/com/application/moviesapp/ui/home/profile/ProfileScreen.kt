package com.application.moviesapp.ui.home.profile

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column {
        Text(text = "Profile Screen")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        ProfileScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ProfileScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        ProfileScreen()
    }
}