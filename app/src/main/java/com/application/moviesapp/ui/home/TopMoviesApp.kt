package com.application.moviesapp.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMoviesApp(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { TopMoviesTopAppbar() }
    ) { paddingValues ->
        TopMoviesScreen(modifier = modifier.padding(paddingValues))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopMoviesTopAppbar() {
    TopAppBar(
        title = {},
        navigationIcon = { Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null) },
        actions = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
            }
        }
    )
}