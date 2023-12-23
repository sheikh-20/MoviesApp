package com.application.moviesapp.ui.onboarding.forgotpassword

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.R
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun CreateNewPasswordScreen(modifier: Modifier = Modifier) {

    val focusManager = LocalFocusManager.current

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {

        Image(
            painter = painterResource(id = R.drawable.ic_reset_password_two),
            contentDescription = null,
            modifier = modifier.size(350.dp),
            contentScale = ContentScale.Crop,
        )

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            Text(
                text = "Create your new password",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                modifier = modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = "",
                onValueChange = {  },
                label = { Text(text = "Password") },
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Lock, contentDescription = null)
                },
                modifier = modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        modifier = modifier.size(24.dp),
                        imageVector = Icons.Rounded.VisibilityOff,
                        contentDescription = null)
                },
                shape = RoundedCornerShape(30),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = Color.LightGray)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {  },
                label = { Text(text = "Password") },
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Lock, contentDescription = null)
                },
                modifier = modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        modifier = modifier.size(24.dp),
                        imageVector = Icons.Rounded.VisibilityOff,
                        contentDescription = null)
                },
                shape = RoundedCornerShape(30),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = Color.LightGray)
            )

            Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Checkbox(checked = false, onCheckedChange = {})
                Text(text = "Remember me", style = MaterialTheme.typography.labelLarge)
            }
        }

        Button(onClick = {  },
            modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    ambientColor = MaterialTheme.colorScheme.outlineVariant,
                    spotColor = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(50)
                )
                .fillMaxWidth()
                .requiredHeight(50.dp)) {

            Text(text = "Continue",
                modifier = modifier.padding(4.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateNewPasswordLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        CreateNewPasswordScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CreateNewPasswordDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        CreateNewPasswordScreen()
    }
}