package com.application.moviesapp.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.application.moviesapp.base.BaseActivity
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.viewmodel.DetailsViewModel
import com.application.moviesapp.ui.viewmodel.MyListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity() {

    companion object {
        const val MOVIE_ID = "MOVIE_ID"

        fun startActivity(activity: Activity?, movieId: Int?) {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(MOVIE_ID, movieId)
            activity?.startActivity(intent)
        }
    }

    private val viewModel: DetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()

        val movieId = intent.getIntExtra(MOVIE_ID, 0)
        viewModel.getMovieDetail(movieId)
        viewModel.getMovieTrailer(movieId)
        viewModel.getMovieState(movieId)

        setContent {
            MoviesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DetailScreenApp()
                }
            }
        }
    }
}