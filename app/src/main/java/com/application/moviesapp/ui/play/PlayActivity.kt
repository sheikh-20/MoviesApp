package com.application.moviesapp.ui.play

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.moviesapp.base.BaseActivity
import com.application.moviesapp.ui.detail.DetailActivity
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.viewmodel.PlayerViewModel
import com.application.moviesapp.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayActivity: BaseActivity() {

    companion object {
        const val FILE_PATH = "file_path"
        const val VIDEO_TITLE = "video_title"
        const val VIDEO_ID = "video_id"
        const val FROM_SCREEN = "from_screen"

        fun startActivity(activity: Activity?, videoTitle: String?, filePath: String?, videoId: String?, fromScreen: Screen?) {
            val intent = Intent(activity, PlayActivity::class.java)
            intent.putExtra(VIDEO_TITLE, videoTitle)
            intent.putExtra(FILE_PATH, filePath)

            //Detail screen
            intent.putExtra(VIDEO_ID, videoId)
            intent.putExtra(FROM_SCREEN, fromScreen?.title)
            activity?.startActivity(intent)
        }
    }

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()

        lifecycle.coroutineScope.launch {
            profileViewModel.isDarkMode.collect {
                setContent {
                    MoviesAppTheme(darkTheme = it.data) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.scrim
                        ) {
                            PlayScreenApp()
                        }
                    }
                }
            }
        }
    }
}