package com.application.moviesapp.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Comment
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationApp(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            NotificationTopAppbar()
        }
    ) { paddingValues ->
        NotificationScreen(modifier = modifier.padding(paddingValues))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationTopAppbar() {
    TopAppBar(
        title = {},
        navigationIcon = { Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null) },
        actions = {
            Icon(imageVector = Icons.Rounded.Comment, contentDescription = null)
        })
}