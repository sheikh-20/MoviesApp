package com.application.moviesapp.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.coroutineScope
import com.application.moviesapp.base.BaseActivity
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.viewmodel.DetailsViewModel
import com.application.moviesapp.ui.viewmodel.ProfileViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

enum class IS_TYPE { Movie, TvSeries }
@AndroidEntryPoint
class DetailActivity : BaseActivity() {

    companion object {
        const val TYPE = "type"
        const val ID = "id"

        fun startActivity(activity: Activity?, type: IS_TYPE?, id: Int?) {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(TYPE, type?.name)
            intent.putExtra(ID, id)
            activity?.startActivity(intent)
        }
    }

    private val viewModel: DetailsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()

        val movieId = intent.getIntExtra(ID, 0)

        when (intent.getStringExtra(TYPE)) {
            IS_TYPE.Movie.name -> {
                viewModel.getMovieDetail(intent.getIntExtra(ID, 0))
                viewModel.getMovieTrailer(intent.getIntExtra(ID, 0))
            }
            IS_TYPE.TvSeries.name -> {
                viewModel.getTvSeriesDetail(intent.getIntExtra(ID, 0))
                viewModel.getTvSeriesTrailer(intent.getIntExtra(ID, 0))
            }
        }

        viewModel.getMovieState(movieId)


        lifecycle.coroutineScope.launch {
            profileViewModel.isDarkMode.collect {
                setContent {
                    MoviesAppTheme(darkTheme = it.data) {
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
    }
}