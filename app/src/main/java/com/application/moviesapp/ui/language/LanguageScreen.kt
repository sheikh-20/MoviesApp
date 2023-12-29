package com.application.moviesapp.ui.language

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.ui.theme.MoviesAppTheme

@Composable
fun LanguageScreen(modifier: Modifier = Modifier, paddingValues: PaddingValues = PaddingValues()) {

    var languageSelected by remember { mutableStateOf(language[0].language[0]) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),) {

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            itemsIndexed(language, ) { index, it ->
                Text(text = it.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)

                it.language.forEachIndexed { lanIndex, lan ->
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(text = lan, modifier = modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)

                        RadioButton(selected = languageSelected ==  lan, onClick = { languageSelected = lan })
                    }
                }

                if (index != language.size.dec()) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LanguageLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        LanguageScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LanguageDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        LanguageScreen()
    }
}