package com.application.moviesapp.ui.download

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun DownloadApp(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { DownloadTopAppbar() }
    ) {
        DownloadTopAppbar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DownloadTopAppbar(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    TopAppBar(
        title = { Text(text = "Download") },
        navigationIcon = {
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
}