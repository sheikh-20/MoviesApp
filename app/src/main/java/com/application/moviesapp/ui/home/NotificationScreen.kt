package com.application.moviesapp.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.R
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun NotificationScreen(modifier: Modifier = Modifier, paddingValues: PaddingValues = PaddingValues()) {
    Box(modifier = modifier
        .fillMaxSize()
        .padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        Column() {

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                        .padding(32.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_empty_list),
                        contentDescription = null,
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                            .size(200.dp),
                        contentScale = ContentScale.Crop,
                    )

                    Text(
                        text = stringResource(R.string.empty),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    )

                    Text(
                        text = stringResource(R.string.you_did_not_receive_any_notification),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    )
                }
            }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        NotificationScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun NotificationScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        NotificationScreen()
    }
}