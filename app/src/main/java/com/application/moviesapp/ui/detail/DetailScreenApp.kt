package com.application.moviesapp.ui.detail

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun DetailScreenApp(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { DetailTopAppbar() },
        containerColor = Color.Transparent) { paddingValues ->
        DetailScreen(modifier = modifier.padding(paddingValues))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTopAppbar(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    TopAppBar(
        title = {  },
        navigationIcon = {
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Rounded.Cast, contentDescription = null)
            }
        })
}