package com.application.moviesapp.ui.onboarding.signup

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun CreateNewPinScreen(modifier: Modifier = Modifier, paddingValues: PaddingValues = PaddingValues(), onContinueClick: () -> Unit = {}) {

    val focusManager = LocalFocusManager.current

    var pinCode by remember {
        mutableStateOf("")
    }
    var isChecked by remember { mutableStateOf(false) }

    Column(modifier = modifier
        .fillMaxSize().wrapContentSize(align = Alignment.Center)
        .padding(start = 16.dp, end = 16.dp, top = 100.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(50.dp)) {

        Text(text = "Add a PIN number to make your account more secure",
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.padding(horizontal = 16.dp), 
            textAlign = TextAlign.Center)

        Row(modifier = modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = pinCode,
                onValueChange = {
                    if (it.length <= 4) {
                        pinCode = it
                    }
                },
                modifier = modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true,
                decorationBox = {
                    Row(horizontalArrangement = Arrangement.Center) {
                        repeat(4) {
                            val char = when {
                                it >= pinCode.length -> ""
                                else -> pinCode[it].toString()
                            }

                            val isFocused = pinCode.length == it

                            isChecked = pinCode.length == 4

                            androidx.compose.material.Text(
                                text = char,
                                modifier = modifier
                                    .size(width = 70.dp, height = 50.dp)
                                    .border(
                                        if (isFocused) 2.dp
                                        else 1.dp,
                                        if (isFocused) MaterialTheme.colorScheme.primary
                                        else Color.LightGray, RoundedCornerShape(20)
                                    )
                                    .padding(2.dp),
                                style = MaterialTheme.typography.headlineMedium,
                                textAlign = TextAlign.Center)

                            Spacer(modifier = modifier.width(16.dp))
                        }
                    }
                },
            )
        }

        Spacer(modifier = modifier.weight(1f))

        Button(onClick = onContinueClick, modifier =  modifier
            .shadow(
                elevation = 4.dp,
                ambientColor = MaterialTheme.colorScheme.outlineVariant,
                spotColor = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(50)
            )
            .fillMaxWidth()) {
            Text(text = "Continue", modifier = modifier.padding(4.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateNewPinScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        CreateNewPinScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CreateNewPinScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        CreateNewPinScreen()
    }
}