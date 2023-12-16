package com.application.moviesapp.ui.download

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.ui.editprofile.EditProfileActivity
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun DownloadScreen(modifier: Modifier = Modifier, paddingValues: PaddingValues = PaddingValues()) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding(), start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Rounded.Wifi, contentDescription = null)

            Spacer(modifier = modifier.width(10.dp))

            Text(text = "Wifi only", modifier = modifier.weight(1f))

            Switch(checked = false, onCheckedChange = {  })
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Outlined.FileDownload, contentDescription = null)

            Spacer(modifier = modifier.width(10.dp))

            Text(text = "Smart Downloads", modifier = modifier.weight(1f))

            IconButton(onClick = {  }) {
                Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
            }
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Rounded.Videocam, contentDescription = null)

            Spacer(modifier = modifier.width(10.dp))

            Text(text = "Video Quality", modifier = modifier.weight(1f))

            IconButton(onClick = {  }) {
                Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
            }
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Rounded.Mic, contentDescription = null)

            Spacer(modifier = modifier.width(10.dp))

            Text(text = "Audio Quality", modifier = modifier.weight(1f))

            IconButton(onClick = {  }) {
                Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
            }
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)

            Spacer(modifier = modifier.width(10.dp))

            Text(text = "Delete All Downloads", modifier = modifier.weight(1f))

            IconButton(onClick = {  }) {

            }
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)

            Spacer(modifier = modifier.width(10.dp))

            Text(text = "Delete Cache", modifier = modifier.weight(1f))

            IconButton(onClick = {  }) {

            }
        }

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