package com.application.moviesapp.ui.home.download

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.ui.play.PlayActivity
import com.application.moviesapp.ui.play.Screen
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.getFileSize
import com.application.moviesapp.ui.utility.getVideoDuration
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.utility.toYoutubeDuration
import com.application.moviesapp.ui.viewmodel.DownloadUiState
import com.application.moviesapp.ui.viewmodel.DownloadsUiState
import timber.log.Timber
import java.io.File

@Composable
fun DownloadScreen(modifier: Modifier = Modifier,
                   downloadUiState: DownloadsUiState = DownloadsUiState(),
                   lazyListState: LazyListState = LazyListState(),
                   bottomPadding: PaddingValues = PaddingValues(),
                   onDeleteClick: (MovieDownloadEntity) -> Unit = { _ ->  }) {

    Column(modifier = modifier
        .fillMaxSize()
        .wrapContentSize(align = Alignment.Center)
        .padding(
            top = bottomPadding.calculateTopPadding(),
            bottom = bottomPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        if (downloadUiState.data.isEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier.padding(32.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_download_empty),
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                        .size(200.dp),
                    contentScale = ContentScale.Crop,
                )

                Text(text = stringResource(R.string.you_downloaded_nothing),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally))

                Text(text = stringResource(R.string.it_seems_you_haven_t_downloaded_any_movies_or_series),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally))
            }
        } else {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = lazyListState,
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp)) {

                items(downloadUiState.data) {
                    DownloadCard(movie = it, onDeleteClick = onDeleteClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun DownloadCard(modifier: Modifier = Modifier,
                         movie: MovieDownloadEntity? = null,
                         onDeleteClick: (MovieDownloadEntity) -> Unit = { _ -> }
) {

    val context = LocalContext.current

    Card(onClick = { PlayActivity.startActivity(activity = context as Activity, videoTitle = movie?.title, filePath = movie?.filePath, videoId = null, fromScreen = Screen.Download) },
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(modifier = modifier.size(height = 110.dp, width = 140.dp),
                shape = RoundedCornerShape(20)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                        .data(movie?.backdropPath ?: "")
                        .crossfade(true)
                        .build(),
                        placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                        contentDescription = null,
                        modifier = modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop)
                    Icon(imageVector = Icons.Rounded.PlayCircle, contentDescription = null, tint = Color.White)
                }
            }

            Column(modifier = modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    text = movie?.title ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = movie?.runtime ?: movie?.filePath?.getVideoDuration(context as Activity) ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                    Card(modifier = modifier.border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = CardDefaults.shape)) {
                        Text(text = "${movie?.filePath?.getFileSize(context)} MB",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onSecondary)
                    }

                    Spacer(modifier = modifier.weight(1f))

                    IconButton(onClick = { onDeleteClick(movie ?: return@IconButton) }) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DownloadScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        DownloadScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DownloadScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        DownloadScreen()
    }
}