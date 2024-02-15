package com.application.moviesapp.ui.onboarding.signup

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.ui.home.HomeActivity
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun SetYourFingerprintScreen(modifier: Modifier = Modifier, onContinueClick: () -> Unit = {}) {

    val context = LocalContext.current

    Column(modifier = modifier
        .fillMaxSize()
        .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Add a fingerprint to make your account more secure",
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center)

        Spacer(modifier = modifier.weight(1f))

        IconButton(onClick = { /*TODO*/ }, modifier = modifier.size(250.dp)) {
            Icon(imageVector = Icons.Outlined.Fingerprint,
                contentDescription = null,
                modifier = modifier.size(250.dp),
                tint = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = modifier.weight(1f))

        Text(text = "Please put your finger on the fingerprint scanner to get started.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center)


        Row(modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = { /*TODO*/ },
                modifier =  modifier
                    .shadow(
                        elevation = 4.dp,
                        ambientColor = MaterialTheme.colorScheme.outlineVariant,
                        spotColor = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(50)
                    )
                    .weight(1f)) {
                Text(text = "Skip",  modifier = modifier.padding(4.dp))
            }
            Button(onClick = onContinueClick,
                modifier =  modifier
                    .shadow(
                        elevation = 4.dp,
                        ambientColor = MaterialTheme.colorScheme.outlineVariant,
                        spotColor = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(50)
                    ).weight(1f)) {
                Text(text = "Continue",  modifier = modifier.padding(4.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SetYourFingerprintScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        SetYourFingerprintScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SetYourFingerprintDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        SetYourFingerprintScreen()
    }
}