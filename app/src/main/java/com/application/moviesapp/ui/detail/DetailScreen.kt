package com.application.moviesapp.ui.detail

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.StarHalf
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.domain.Movies
import com.application.moviesapp.domain.model.MovieState
import com.application.moviesapp.domain.model.MovieTrailerWithYoutube
import com.application.moviesapp.domain.model.MovieUpcoming
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.domain.model.Stream
import com.application.moviesapp.ui.play.PlayActivity
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.utility.toOneDecimal
import com.application.moviesapp.ui.utility.toYoutubeDuration
import com.application.moviesapp.ui.viewmodel.DownloadUiState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt

private const val TAG = "DetailScreen"
@OptIn(ExperimentalPagerApi::class)
@Composable
fun DetailScreen(modifier: Modifier = Modifier,
                 uiState: Resource<MoviesDetail> = Resource.Loading,
                 trailerUiState: Resource<List<MovieTrailerWithYoutube>> = Resource.Loading,
                 moviesFlow: LazyPagingItems<MovieUpcoming>,
                 onBookmarkClicked: (String, Int, Boolean) -> Unit = { _, _, _ -> },
                 snackbarHostState: SnackbarHostState = SnackbarHostState(),
                 bookmarkUiState: Resource<MovieState> = Resource.Loading,
                 onTrailerClick: (String) -> Unit = { _ -> },
                 downloaderUiState: DownloadUiState = DownloadUiState.Default,
                 onTrailerDownloadClick: (String, Stream, Stream, MovieDownloadEntity) -> Unit = { _, _, _, _ -> }
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val scrollState = rememberScrollState()
    val scrollToPosition by remember {
        mutableStateOf(0f)
    }

    val items = listOf(
        HorizontalPagerContent("Trailers"),
        HorizontalPagerContent("More Like This"),
        HorizontalPagerContent("Comments"),
        )

    val pager = rememberPagerState()

    var isFavorite by remember { mutableStateOf(true) }
    var isViewMore by remember { mutableStateOf(true) }

    when (uiState) {
        is Resource.Loading -> {
            CircularProgressIndicator(modifier = modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center))
        }
        is Resource.Success -> {

            Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                    .data(uiState.data.backdropPath?.toImageUrl ?: "")
                    .crossfade(true)
                    .build(),
                    placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop)

                Row(modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = uiState.data.originalTitle ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1)

                    when (bookmarkUiState) {
                        is Resource.Loading -> {  }
                        is Resource.Success -> {

                            isFavorite = bookmarkUiState.data.favorite == true

                            IconButton(onClick = {
                                isFavorite = bookmarkUiState.data.favorite != true

                                onBookmarkClicked("movie", uiState.data.id ?: 0, bookmarkUiState.data.favorite != true)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = "Bookmark updated")
                                }
                            }
                        ) {

                                if (isFavorite) {
                                    Icon(imageVector = Icons.Rounded.Bookmark, contentDescription = null)
                                } else {
                                    Icon(imageVector = Icons.Rounded.BookmarkBorder, contentDescription = null)
                                }

                        } }


                        is Resource.Failure -> {}
                    }

                    Icon(imageVector = Icons.Rounded.Share, contentDescription = null)
                }

                Row(modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Rounded.StarHalf, contentDescription = null)

                    Text(text = uiState.data.voteAverage?.toOneDecimal ?: "", style = MaterialTheme.typography.bodyMedium)

                    IconButton(modifier = modifier.then(Modifier.size(20.dp)), onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                    }

                    Row(modifier = modifier.horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = uiState.data.releaseDate?.split("-")?.get(0) ?: "", style = MaterialTheme.typography.bodyMedium)

                        AssistChip(
                            onClick = { },
                            label = {
                                Text(text = "13+", style = MaterialTheme.typography.bodySmall)
                            },
                            modifier = modifier.requiredHeight(30.dp)
                        )

                        AssistChip(
                            onClick = { /*TODO*/ },
                            label = {
                                Text(text = "United States",  style = MaterialTheme.typography.bodySmall)
                            },
                            modifier = modifier.requiredHeight(30.dp))

                        AssistChip(
                            onClick = { /*TODO*/ },
                            label = {
                                Text(text = "Subtitle",  style = MaterialTheme.typography.bodySmall)
                            },
                            modifier = modifier.requiredHeight(30.dp))
                    }
                }

                Row(modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    Button(onClick = { PlayActivity.startActivity(context as Activity) }, modifier = modifier.weight(1f)) {
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

                Text(text = "Genre: ${uiState.data.genres}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),)

                if ((uiState.data.overview?.length ?: 0) >= 200) {
                    if (isViewMore) {
                        ClickableText(
                            text = buildAnnotatedString {
                                append(uiState.data.overview?.take(200) ?: "")
                                append("...")
                                append("\t")
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)) {
                                    append("View More")
                                }
                            },
                            style = TextStyle.Default.copy(
                                fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                                color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                                lineHeightStyle = MaterialTheme.typography.bodySmall.lineHeightStyle,
                                drawStyle = MaterialTheme.typography.bodySmall.drawStyle,
                                platformStyle = MaterialTheme.typography.bodySmall.platformStyle,
                                letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight),
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            onClick = {
                                isViewMore = !isViewMore
                                if (!isViewMore) {
                                    coroutineScope.launch {
                                        scrollState.animateScrollTo(value = uiState.data.overview?.length ?: 0)
                                    }
                                }
                            },
                        )
                    } else {
                        Column(modifier = modifier
                            .height(100.dp)
                            .verticalScroll(state = scrollState)) {

                            ClickableText(
                                text = buildAnnotatedString {
                                    append(uiState.data.overview ?: "")
                                    append("\t")
                                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)) {
                                        append("View Less")
                                    }
                                },
                                style = TextStyle.Default.copy(
                                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                                    color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                                    lineHeightStyle = MaterialTheme.typography.bodySmall.lineHeightStyle,
                                    drawStyle = MaterialTheme.typography.bodySmall.drawStyle,
                                    platformStyle = MaterialTheme.typography.bodySmall.platformStyle,
                                    letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight),
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                onClick = { isViewMore = !isViewMore },
                            )
                        }
                    }
                } else {
                    Text(text = uiState.data.overview.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),)
                }

                LazyRow(modifier = modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                    items(count = uiState.data.cast?.size ?: 0) { index ->

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                                .data(uiState.data.cast?.get(index)?.profilePath?.toImageUrl ?: "")
                                .crossfade(true)
                                .build(),
                                placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                                contentDescription = null,
                                modifier = modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(50)),
                                contentScale = ContentScale.Crop)

                            Column {
                                Text(text = uiState.data.cast?.get(index)?.name ?: "", style = MaterialTheme.typography.bodySmall)
                                Text(text = uiState.data.cast?.get(index)?.character ?: "", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                TabRow(selectedTabIndex = pager.currentPage) {
                    items.forEachIndexed { index, horizontalPagerContent ->
                        Tab(selected = pager.currentPage == index,
                            onClick = { /*TODO*/ },
                            text = {
                                Text(text = items[index].title)
                            })
                    }
                }

                HorizontalPager(count = items.size, state = pager, modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)) { index ->
                    when (index) {
                        0 -> { 
                            when(trailerUiState) {
                                is Resource.Loading -> { CircularProgressIndicator() }
                                is Resource.Failure -> { Text(text = "Failed!") }
                                is Resource.Success -> {
                                    LazyColumn(
                                        modifier = modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp)
                                    ) {
                                        items(trailerUiState.data) { trailer ->
                                            TrailerCard(
                                                movieTrailerWithYoutube = trailer,
                                                onTrailerClick = onTrailerClick,
                                                downloaderUiState = downloaderUiState,
                                                onTrailerDownloadClick = onTrailerDownloadClick,
                                                movieDetail = uiState.data)
                                        }
                                    }
                                }
                            }
                        }
                        1 -> {
                            LazyVerticalGrid(columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                            ) {

                                items(moviesFlow.itemCount) { index ->
                                    MovieImageCard(imageUrl = moviesFlow[index]?.posterPath ?: "", rating = moviesFlow[index]?.voteAverage.toString() ?: "", movieId = moviesFlow[index]?.movieId ?: 0)
                                }
                            }
                        }
                        2 -> {
                            Text(text = "Comments")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieImageCard(modifier: Modifier = Modifier, imageUrl: String = "", rating: String = "", movieId: Int? = null) {

    val context = LocalContext.current

    Timber.tag("Card").d(movieId.toString())

    Card(shape = RoundedCornerShape(10), onClick = { DetailActivity.startActivity(context as Activity, movieId) }) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(imageUrl.toImageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.height(250.dp),
            )

            Card(modifier = modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.TopStart)
                .padding(8.dp), shape = RoundedCornerShape(30), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Text(text = rating, modifier = modifier.padding(horizontal = 10.dp, vertical = 8.dp), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
private fun TrailerCard(modifier: Modifier = Modifier,
                        movieTrailerWithYoutube: MovieTrailerWithYoutube = MovieTrailerWithYoutube("", "Iron man", "", "1 min 20sec"),
                        onTrailerClick: (String) -> Unit = { _ -> },
                        downloaderUiState: DownloadUiState = DownloadUiState.Default,
                        movieDetail: MoviesDetail? = null,
                        onTrailerDownloadClick: (String, Stream, Stream, MovieDownloadEntity) -> Unit = { _, _, _, _ -> },
                        ) {

    val context = LocalContext.current

    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(modifier = modifier.size(height = 100.dp, width = 130.dp),
            shape = RoundedCornerShape(20)) {
            Box(contentAlignment = Alignment.Center) {
                AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                    .data(movieTrailerWithYoutube.thumbnail ?: "")
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

        Column(modifier = modifier.weight(1f), verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = movieTrailerWithYoutube.title ?: "",
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = movieTrailerWithYoutube.duration?.toYoutubeDuration ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = modifier.weight(1f))

                when (downloaderUiState) {
                    is DownloadUiState.Default -> {
                        Timber.tag(TAG).d("Video Downloader")
                    }
                    is DownloadUiState.Loading -> {
                        Timber.tag(TAG).d("Downloader Loading")
                        CircularProgressIndicator(modifier = modifier.size(28.dp), strokeWidth = 4.dp, strokeCap = StrokeCap.Round)
                    }
                    is DownloadUiState.Complete -> {
                        Timber.tag(TAG).d(downloaderUiState.toString())

                        AssistChip(
                            onClick = {
                                onTrailerDownloadClick(movieTrailerWithYoutube.id ?: return@AssistChip,
                                    downloaderUiState.videoStreams.first(),
                                    downloaderUiState.audioStreams ?: return@AssistChip,
                                    MovieDownloadEntity(
                                        backdropPath = movieTrailerWithYoutube.thumbnail,
                                        runtime = movieTrailerWithYoutube.duration?.toYoutubeDuration ?: "",
                                        title = movieTrailerWithYoutube.title
                                    ))
                            },
                            label = {
                                Text(text = downloaderUiState.videoStreams.first().resolution.removeSurrounding("\""), color = MaterialTheme.colorScheme.primary)
                            },
                            shape = RoundedCornerShape(50)
                        )
                    }
                }

                IconButton(onClick = { onTrailerClick(movieTrailerWithYoutube.id ?: return@IconButton) }) {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun SpannableText(text: String = "") {
    ClickableText(text = buildAnnotatedString {
        append(text)
        withStyle(style = SpanStyle(Color.Blue)) {
            append("View More")
        }
    }, onClick = {})
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
//        DetailScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DetailScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
//        DetailScreen()
    }
}