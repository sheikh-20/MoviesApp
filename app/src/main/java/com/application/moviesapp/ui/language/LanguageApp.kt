package com.application.moviesapp.ui.language

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.moviesapp.domain.model.LanguagePreference
import com.application.moviesapp.ui.viewmodel.ProfileViewModel

@Composable
fun LanguageApp(modifier: Modifier = Modifier, profileViewModel: ProfileViewModel = hiltViewModel()) {

    val languageUIState by profileViewModel.selectLanguage.collectAsState(initial = LanguagePreference(language[0].language[0]))

    Scaffold(
        topBar = {
            LanguageTopAppbar()
        }
    ) { paddingValues ->
        LanguageScreen(paddingValues = paddingValues, languageUIState = languageUIState, onLanguageClick = profileViewModel::updateLanguage)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageTopAppbar(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    TopAppBar(
        title = { Text(text = "Language") },
        navigationIcon = {
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
}