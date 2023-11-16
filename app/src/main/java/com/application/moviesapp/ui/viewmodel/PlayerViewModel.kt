package com.application.moviesapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(val player: Player): ViewModel() {
    
    fun playVideo(context: Context, filePath: String) {
        player.setMediaItem(MediaItem.fromUri(
            File(context.filesDir, "output/$filePath").toString()
        ))
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    init {
        player.prepare()
    }
}