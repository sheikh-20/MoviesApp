package com.application.moviesapp.ui.notification

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.domain.model.AppUpdatesPreference
import com.application.moviesapp.domain.model.GeneralNotificationPreference
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun NotificationScreen(modifier: Modifier = Modifier,
                       paddingValues: PaddingValues = PaddingValues(),
                       generalNotificationPreference: GeneralNotificationPreference = GeneralNotificationPreference(false),
                       appUpdatesPreference: AppUpdatesPreference = AppUpdatesPreference(false),
                       onGeneralNotificationChange: (Boolean) -> Unit = { _ -> },
                       onAppUpdateChange: (Boolean) -> Unit = { _ -> }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding(), start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = "General Notification", modifier = modifier.weight(1f))

            Switch(checked = generalNotificationPreference.data, onCheckedChange = onGeneralNotificationChange)
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = "App Updates", modifier = modifier.weight(1f))

            Switch(checked = appUpdatesPreference.data, onCheckedChange = onAppUpdateChange)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        NotificationScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun NotificationDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        NotificationScreen()
    }
}