package com.application.moviesapp.ui.play

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.Player
import com.application.moviesapp.ui.viewmodel.PlayerViewModel
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PlayScreenApp(modifier: Modifier = Modifier, player: Player? = null) {

    val systemUiController: SystemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false
    systemUiController.isSystemBarsVisible = false

    Scaffold(
        containerColor = Color.Transparent,
    ) { paddingValues ->
        PlayScreen(player = player)
    }

}