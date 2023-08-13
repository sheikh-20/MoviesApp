package com.application.moviesapp.ui.onboarding.login

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.R
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun LoginScreen(modifier: Modifier = Modifier, onSignInClick: () -> Unit = {}) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, 
        verticalArrangement = Arrangement.SpaceBetween) {

        Image(painter = painterResource(id = R.drawable.ic_login),
            contentDescription = null,
            modifier = modifier.size(200.dp))

        Text(text = stringResource(R.string.let_s_you_in),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.SemiBold)

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            LoginComponent(icon = R.drawable.ic_facebook, text = R.string.continue_with_facebook) {

            }
            LoginComponent(icon = R.drawable.ic_google, text = R.string.continue_with_google) {

            }
            LoginComponent(icon = R.drawable.ic_github, text = R.string.continue_with_github) {

            }
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Divider(modifier = modifier.weight(1f), color = Color.LightGray)
            Text(text = stringResource(R.string.or))
            Divider(modifier = modifier.weight(1f), color = Color.LightGray)
        }

        Button(onClick = onSignInClick,
            modifier = modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color.Red)) {
            Text(text = stringResource(id = R.string.signin_with_password), color = colorResource(id = R.color.white), modifier = modifier.padding(4.dp))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.don_t_have_an_account))
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = stringResource(R.string.sign_up), color = Color.Red)
            }
        }
    }
}

@Composable
private fun LoginComponent(modifier: Modifier = Modifier, @DrawableRes icon: Int, @StringRes text: Int, onClick: () -> Unit) {
    Button(onClick = { /*TODO*/ },
        shape = RoundedCornerShape(30),
        border = BorderStroke(width = .5.dp, color =  Color.LightGray),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {

        Row(modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Icon(painter = painterResource(id = icon),
                contentDescription = null,
                modifier = modifier.size(30.dp))

            Spacer(modifier = modifier.padding(horizontal = 8.dp))

            Text(text = stringResource(id = text),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(id = R.color.black))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        LoginScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        LoginScreen()
    }
}