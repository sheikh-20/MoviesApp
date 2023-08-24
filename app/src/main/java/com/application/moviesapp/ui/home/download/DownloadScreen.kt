package com.application.moviesapp.ui.home.download

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.R
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun DownloadScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .wrapContentSize(align = Alignment.Center)
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Column(verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier.padding(32.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_download_empty),
                contentDescription = null,
                modifier = modifier.fillMaxWidth().wrapContentWidth(align = Alignment.CenterHorizontally).size(200.dp),
                contentScale = ContentScale.Crop,
            )

            Text(text = "You Downloaded Nothing",
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.fillMaxWidth().wrapContentWidth(align = Alignment.CenterHorizontally))

            Text(text = "It seems you haven't downloaded any movies or series",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier.fillMaxWidth().wrapContentWidth(align = Alignment.CenterHorizontally))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DownloadScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        DownloadScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DownloadScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        DownloadScreen()
    }
}