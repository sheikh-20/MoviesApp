package com.application.moviesapp.ui.onboarding.signup

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.R
import com.application.moviesapp.ui.theme.MoviesAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillYourProfileScreen(modifier: Modifier = Modifier, onContinueClick: () -> Unit = {}) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Image(imageVector = Icons.Outlined.AccountCircle,
            contentDescription = null,
            modifier = modifier.size(200.dp))

        Column(modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Full Name") },
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Nick Name") },
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Email") },
                trailingIcon = { Icon(imageVector = Icons.Rounded.Email, contentDescription = null) },
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Phone Number") },
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Gender") },
                trailingIcon = { Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = null
                ) },
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30))
        }

        Row(modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = { /*TODO*/ }, modifier = modifier.weight(1f)) {
                Text(text = "Skip")
            }
            Button(onClick = onContinueClick, modifier = modifier.weight(1f)) {
                Text(text = "Continue")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FillYourProfileScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        FillYourProfileScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FillYourProfileScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        FillYourProfileScreen()
    }
}