package com.application.moviesapp.ui.home.profile

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.model.SettingsPreference
import com.application.moviesapp.ui.editprofile.EditProfileActivity
import com.application.moviesapp.ui.signin.UserData
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import timber.log.Timber

private const val TAG = "ProfileScreen"
@Composable
fun ProfileScreen(modifier: Modifier = Modifier,
                  uiState: UserData? = null,
                  onSignOutClick: () -> Unit = {},
                  darkModeUiState: SettingsPreference = SettingsPreference(false),
                  onModeClick: (Boolean) -> Unit = {},
                  onProfileClick: (Uri) -> Unit = { _ -> },
                  profileUIState: Resource<Uri> = Resource.Loading) {

    val context = LocalContext.current

    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            Timber.tag(TAG).d(uri.toString())
            onProfileClick(uri)
        }
    }

    Column(modifier = modifier
        .fillMaxSize()
        .wrapContentSize(align = Alignment.Center)
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            IconButton(onClick = { getContent.launch("image/*") }, modifier = modifier.size(100.dp)) {

                when (profileUIState) {
                    is Resource.Loading -> {
                        Icon(painterResource(id = R.drawable.ic_image_placeholder),
                            contentDescription = null,
                            modifier = modifier.size(100.dp))
                    }

                    is Resource.Failure -> {
                        Icon(imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = null,
                            modifier = modifier.size(100.dp))
                    }

                    is Resource.Success -> {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(profileUIState.data)
                                .crossfade(true)
                                .build(),
                            error = painterResource(id = R.drawable.ic_broken_image),
                            placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = modifier.size(100.dp),
                        )
                    }
                }
            }
            
            Text(text = uiState?.userName ?: "",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold)
            
            Text(text = uiState?.email ?: "", style = MaterialTheme.typography.bodyLarge)
        }

        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            Row(modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = null)

                Spacer(modifier = modifier.width(10.dp))

                Text(text = "Edit Profile", modifier = modifier.weight(1f))

                IconButton(onClick = { EditProfileActivity.startActivity((context as Activity)) }) {
                    Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                }
            }

            Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null)

                Spacer(modifier = modifier.width(10.dp))

                Text(text = "Notification", modifier = modifier.weight(1f))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                }
            }

            Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.FileDownload, contentDescription = null)

                Spacer(modifier = modifier.width(10.dp))

                Text(text = "Download", modifier = modifier.weight(1f))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                }
            }

            Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.Security, contentDescription = null)

                Spacer(modifier = modifier.width(10.dp))

                Text(text = "Security", modifier = modifier.weight(1f))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                }
            }

            Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.Language, contentDescription = null)

                Spacer(modifier = modifier.width(10.dp))

                Text(text = "Language", modifier = modifier.weight(1f))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                }
            }


            Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = if (darkModeUiState.data) Icons.Outlined.DarkMode else Icons.Outlined.LightMode, contentDescription = null)

                Spacer(modifier = modifier.width(10.dp))

                Text(text = if (darkModeUiState.data) "Dark Mode" else "Light Mode", modifier = modifier.weight(1f))

                Switch(checked = darkModeUiState.data, onCheckedChange = onModeClick)
            }

            Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = null)

                Spacer(modifier = modifier.width(10.dp))

                Text(text = "Help Center", modifier = modifier.weight(1f))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                }
            }


            Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.PrivacyTip, contentDescription = null)

                Spacer(modifier = modifier.width(10.dp))

                Text(text = "Privacy Policy", modifier = modifier.weight(1f))

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                }
            }

            Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Outlined.Logout, contentDescription = null)

                Spacer(modifier = modifier.width(10.dp))

                Text(text = "Logout", modifier = modifier.weight(1f))

                IconButton(onClick = onSignOutClick) {
                    Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                }
            }
        }

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