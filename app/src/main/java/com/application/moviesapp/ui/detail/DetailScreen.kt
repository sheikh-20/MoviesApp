package com.application.moviesapp.ui.detail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.Movies
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import timber.log.Timber

@Composable
fun DetailScreen(modifier: Modifier = Modifier, uiState: Resource<MoviesDetail> = Resource.Loading) {

    when (uiState) {
        is Resource.Loading -> {
            CircularProgressIndicator(modifier = modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center))
        }
        is Resource.Success -> {
            Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                    .data(uiState.data.posterPath?.toImageUrl ?: "")
                    .crossfade(true)
                    .build(),
                    placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = uiState.data.originalTitle ?: "")

                    Spacer(modifier = modifier.weight(1f))

                    Icon(imageVector = Icons.Rounded.BookmarkBorder, contentDescription = null)
                    Icon(imageVector = Icons.Rounded.Share, contentDescription = null)
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Rounded.StarHalf, contentDescription = null)
                    Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                    Text(text = uiState.data.releaseDate?.split("-")?.get(0) ?: "", fontSize = 12.sp)
                    OutlinedCard { Text(text = "13+", fontSize = 12.sp) }
                    OutlinedCard { Text(text = "United States", fontSize = 12.sp) }
                    OutlinedCard { Text(text = "Subtitle", fontSize = 12.sp) }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    Button(onClick = { /*TODO*/ }, modifier = modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Rounded.PlayCircle, contentDescription = null)
                            Text(text = "Play")
                        }
                    }

                    OutlinedButton(onClick = { /*TODO*/ }, modifier = modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Outlined.FileDownload, contentDescription = null)
                            Text(text = "Download")
                        }
                    }
                }

                Text(text = "Genre: ${uiState.data.genres?.joinToString(", ")}")

                Text(text = uiState.data.overview.toString())

                LazyRow(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(count = uiState.data.cast?.size ?: 0) { index ->

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                                .data(uiState.data.cast?.get(index)?.profilePath?.toImageUrl ?: "")
                                .crossfade(true)
                                .build(),
                                placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                                contentDescription = null,
                                modifier = modifier.size(50.dp).clip(RoundedCornerShape(50)),
                                contentScale = ContentScale.Crop)

                            Column {
                                Text(text = uiState.data.cast?.get(index)?.name ?: "")
                                Text(text = uiState.data.cast?.get(index)?.character ?: "")
                            }
                        }

                    }
                }
            }
        }
        is Resource.Failure -> {
            Column(modifier = modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)) {
                Text(text = "Failure")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        DetailScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DetailScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        DetailScreen()
    }
}