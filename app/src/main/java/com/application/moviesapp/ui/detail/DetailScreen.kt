package com.application.moviesapp.ui.detail

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Comment
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Favorite
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.domain.model.Comment
import com.application.moviesapp.domain.model.CommentRepository
import com.application.moviesapp.domain.model.MovieNowPlaying
import com.application.moviesapp.domain.model.MovieState
import com.application.moviesapp.domain.model.MovieTrailerWithYoutube
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.domain.model.Stream
import com.application.moviesapp.domain.model.TvSeriesDetail
import com.application.moviesapp.domain.model.TvSeriesEpisodes
import com.application.moviesapp.domain.model.TvSeriesTrailerWithYoutube
import com.application.moviesapp.ui.play.PlayActivity
import com.application.moviesapp.ui.play.Screen
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

private const val TAG = "DetailScreen"
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(modifier: Modifier = Modifier,
                 movieUIState: Resource<MoviesDetail> = Resource.Loading,
                 tvSeriesUIState: Resource<TvSeriesDetail> = Resource.Loading,
                 moviesTrailerUiState: Resource<List<MovieTrailerWithYoutube>> = Resource.Loading,
                 tvSeriesTrailerUiState: Resource<List<TvSeriesTrailerWithYoutube>> = Resource.Loading,
                 moviesFlow: LazyPagingItems<MovieNowPlaying>,
                 onBookmarkClicked: (String, Int, Boolean) -> Unit = { _, _, _ -> },
                 snackbarHostState: SnackbarHostState = SnackbarHostState(),
                 bookmarkUiState: Resource<MovieState> = Resource.Loading,
                 onTrailerClick: (String) -> Unit = { _ -> },
                 downloaderUiState: DownloadUiState = DownloadUiState.Default,
                 onTrailerDownloadClick: (String, Stream, Stream, MovieDownloadEntity) -> Unit = { _, _, _, _ -> },
                 tvSeriesEpisodesUIState: Resource<TvSeriesEpisodes> = Resource.Loading,
                 onTvSeriesEpisode: (Int, Int) -> Unit = { _, _ -> },
                 onSeasonClick: () -> Unit = {  },
                 onCastClick: (Int) -> Unit = { _ ->  }
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val scrollState = rememberScrollState()
    val overviewScrollState = rememberScrollState()

    val scrollToPosition by remember {
        mutableStateOf(0f)
    }

    val items = listOf(
        HorizontalPagerContent(stringResource(R.string.trailers)),
        HorizontalPagerContent(stringResource(R.string.more_like_this)),
        HorizontalPagerContent(stringResource(R.string.comments)),
        )

    val pager = rememberPagerState()

    var isFavorite by remember { mutableStateOf(true) }
    var isViewMore by remember { mutableStateOf(true) }

    when ((context as Activity).intent.getStringExtra(DetailActivity.TYPE)) {
        IS_TYPE.Movie.name -> {
            when (movieUIState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center))
                }
                is Resource.Success -> {

                    Column(modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                            .data(movieUIState.data.backdropPath?.toImageUrl ?: "")
                            .crossfade(true)
                            .build(),
                            placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                            contentDescription = null,
                            modifier = modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .drawWithCache {
                                    val gradient = Brush.radialGradient(
                                        colors = listOf(Color.Transparent, Color.Black),
                                        center = Offset(200f, 200f),
                                        radius = 1000f
                                    )
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(gradient, blendMode = BlendMode.Multiply)
                                    }
                                },
                            contentScale = ContentScale.Crop)

                        Row(modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(text = movieUIState.data.originalTitle ?: "",
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

                                        onBookmarkClicked("movie", movieUIState.data.id ?: 0, bookmarkUiState.data.favorite != true)
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
                            .horizontalScroll(scrollState)
                            .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Rounded.StarHalf, contentDescription = null)

                            Text(text = movieUIState.data.voteAverage?.toOneDecimal ?: "", style = MaterialTheme.typography.bodyMedium)

                            IconButton(modifier = modifier.then(Modifier.size(16.dp)), onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Rounded.ArrowForwardIos,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary)
                            }

                            Text(text = movieUIState.data.releaseDate?.split("-")?.firstOrNull() ?: "", style = MaterialTheme.typography.bodyMedium)

                            OutlinedButton(
                                onClick = { },
                                modifier = modifier.requiredHeight(30.dp)
                            ) {
                                Text(text = "13+", style = MaterialTheme.typography.bodySmall)
                            }

                            OutlinedButton(
                                onClick = { /*TODO*/ },
                                modifier = modifier.requiredHeight(30.dp)) {
                                Text(text = movieUIState.data.productionCountries?.firstOrNull()?.name ?: "",  style = MaterialTheme.typography.bodySmall)
                            }

                            OutlinedButton(
                                onClick = { /*TODO*/ },
                                modifier = modifier.requiredHeight(30.dp)) {
                                Text(text = "Subtitle",  style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Row(modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                            Button(onClick = {  }, modifier = modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(imageVector = Icons.Rounded.PlayCircle, contentDescription = null)
                                    Text(text = stringResource(id = R.string.play))
                                }
                            }

                            OutlinedButton(onClick = { /*TODO*/ }, modifier = modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(imageVector = Icons.Outlined.FileDownload, contentDescription = null)
                                    Text(text = stringResource(id = R.string.download))
                                }
                            }
                        }

                        Text(
                            text = stringResource(R.string.genre, movieUIState.data.genres.toString()),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )

                        if ((movieUIState.data.overview?.length ?: 0) >= 200) {
                            if (isViewMore) {
                                ClickableText(
                                    text = buildAnnotatedString {
                                        append(movieUIState.data.overview?.take(200) ?: "")
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
                                                overviewScrollState.animateScrollTo(value = movieUIState.data.overview?.length ?: 0)
                                            }
                                        }
                                    },
                                )
                            } else {
                                Column(modifier = modifier
                                    .height(100.dp)
                                    .verticalScroll(state = overviewScrollState)) {

                                    ClickableText(
                                        text = buildAnnotatedString {
                                            append(movieUIState.data.overview ?: "")
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
                            Text(text = movieUIState.data.overview.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),)
                        }
                        
                        Spacer(modifier = modifier.height(4.dp))

                        LazyRow(modifier = modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                            items(count = movieUIState.data.cast?.size ?: 0) { index ->

                                Row(modifier = modifier.clickable(onClick = { onCastClick(movieUIState.data.cast?.get(index)?.id ?: return@clickable) }, interactionSource = remember { MutableInteractionSource() }, indication = null),
                                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                                        .data(movieUIState.data.cast?.get(index)?.profilePath?.toImageUrl ?: "")
                                        .crossfade(true)
                                        .build(),
                                        placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                                        contentDescription = null,
                                        modifier = modifier
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(50)),
                                        contentScale = ContentScale.Crop)

                                    Column {
                                        Text(text = movieUIState.data.cast?.get(index)?.name ?: "", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                        Text(text = movieUIState.data.cast?.get(index)?.character ?: "", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }

                        Column(modifier = modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            TabRow(selectedTabIndex = pager.currentPage) {
                                items.forEachIndexed { index, horizontalPagerContent ->
                                    Tab(selected = pager.currentPage == index,
                                        onClick = {
                                            coroutineScope.launch {
                                                pager.animateScrollToPage(page = index)
                                            }
                                        },
                                        text = {
                                            Text(text = items[index].title)
                                        })
                                }
                            }

                            HorizontalPager(count = items.size, state = pager, modifier = modifier.fillMaxWidth()) { index ->
                                when (index) {
                                    0 -> {
                                        when(moviesTrailerUiState) {
                                            is Resource.Loading -> { CircularProgressIndicator() }
                                            is Resource.Failure -> { Text(text = "Failed!") }
                                            is Resource.Success -> {
                                                LazyColumn(
                                                    modifier = modifier.fillMaxSize(),
                                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                                                ) {
                                                    items(moviesTrailerUiState.data) { trailer ->
                                                        MovieTrailerCard(
                                                            movieTrailerWithYoutube = trailer,
                                                            onTrailerClick = onTrailerClick,
                                                            downloaderUiState = downloaderUiState,
                                                            onTrailerDownloadClick = onTrailerDownloadClick,
                                                            movieDetail = movieUIState.data)
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
                                                MovieImageCard(imageUrl = moviesFlow[index]?.posterPath ?: "", rating = moviesFlow[index]?.voteAverage?.toOneDecimal ?: "", movieId = moviesFlow[index]?.id ?: 0)
                                            }
                                        }
                                    }
                                    2 -> {
                                         CommentsCompose()
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
        IS_TYPE.TvSeries.name -> {
            when (tvSeriesUIState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center))
                }
                is Resource.Success -> {

                    Column(modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                            .data(tvSeriesUIState.data.backdropPath?.toImageUrl ?: "")
                            .crossfade(true)
                            .build(),
                            placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                            contentDescription = null,
                            modifier = modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .drawWithCache {
                                    val gradient = Brush.radialGradient(
                                        colors = listOf(Color.Transparent, Color.Black),
                                        center = Offset(200f, 200f),
                                        radius = 1000f
                                    )
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(gradient, blendMode = BlendMode.Multiply)
                                    }
                                },
                            contentScale = ContentScale.Crop)

                        Row(modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(text = tvSeriesUIState.data.name ?: "",
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

                                        onBookmarkClicked("movie", tvSeriesUIState.data.id ?: 0, bookmarkUiState.data.favorite != true)
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
                            .horizontalScroll(scrollState)
                            .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Rounded.StarHalf, contentDescription = null)

                            Text(text = tvSeriesUIState.data.voteAverage?.toOneDecimal ?: "", style = MaterialTheme.typography.bodyMedium)

                            IconButton(modifier = modifier.then(Modifier.size(16.dp)), onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Rounded.ArrowForwardIos,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary)
                            }

//
//                            Text(text = tvSeriesUIState.data.releaseDate?.split("-")?.get(0) ?: "", style = MaterialTheme.typography.bodyMedium)

                            OutlinedButton(
                                onClick = { },
                                modifier = modifier.requiredHeight(30.dp)
                            ) {
                                Text(text = "13+", style = MaterialTheme.typography.bodySmall)
                            }

                            OutlinedButton(
                                onClick = { /*TODO*/ },
                                modifier = modifier.requiredHeight(30.dp)) {
                                Text(text = tvSeriesUIState.data.productionCountries?.firstOrNull()?.name ?: "",  style = MaterialTheme.typography.bodySmall)
                            }

                            OutlinedButton(
                                onClick = { /*TODO*/ },
                                modifier = modifier.requiredHeight(30.dp)) {
                                Text(text = "Subtitle",  style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Row(modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                            Button(onClick = {  }, modifier = modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(imageVector = Icons.Rounded.PlayCircle, contentDescription = null)
                                    Text(text = stringResource(id = R.string.play))
                                }
                            }

                            OutlinedButton(onClick = { /*TODO*/ }, modifier = modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(imageVector = Icons.Outlined.FileDownload, contentDescription = null)
                                    Text(text = stringResource(id = R.string.download))
                                }
                            }
                        }

                        Text(text = stringResource(R.string.genre, tvSeriesUIState.data.genres.toString()),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),)

                        if ((tvSeriesUIState.data.overview?.length ?: 0) >= 200) {
                            if (isViewMore) {
                                ClickableText(
                                    text = buildAnnotatedString {
                                        append(tvSeriesUIState.data.overview?.take(200) ?: "")
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
                                                overviewScrollState.animateScrollTo(value = tvSeriesUIState.data.overview?.length ?: 0)
                                            }
                                        }
                                    },
                                )
                            } else {
                                Column(modifier = modifier
                                    .height(100.dp)
                                    .verticalScroll(state = overviewScrollState)) {

                                    ClickableText(
                                        text = buildAnnotatedString {
                                            append(tvSeriesUIState.data.overview ?: "")
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
                            Text(text = tvSeriesUIState.data.overview.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),)
                        }

                        Spacer(modifier = modifier.height(4.dp))

                        LazyRow(modifier = modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                            items(count = tvSeriesUIState.data.cast?.size ?: 0) { index ->

                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                                        .data(tvSeriesUIState.data.cast?.get(index)?.profilePath?.toImageUrl ?: "")
                                        .crossfade(true)
                                        .build(),
                                        placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                                        contentDescription = null,
                                        modifier = modifier
                                            .size(50.dp)
                                            .clip(RoundedCornerShape(50)),
                                        contentScale = ContentScale.Crop)

                                    Column {
                                        Text(text = tvSeriesUIState.data.cast?.get(index)?.name ?: "", style = MaterialTheme.typography.bodySmall)
                                        Text(text = tvSeriesUIState.data.cast?.get(index)?.roles?.first()?.character.toString() ?: "", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(modifier = modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Episodes",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold)

                                Row(modifier = modifier.clickable(onClick = onSeasonClick)) {
                                    Text(text = "Season ${tvSeriesUIState.data.seasons?.firstOrNull()?.seasonNumber?.inc()}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary)
                                    Icon(imageVector = Icons.Rounded.ExpandMore,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary)
                                }
                            }

                            LaunchedEffect(key1 = null) {
                                onTvSeriesEpisode(tvSeriesUIState.data.id ?: 0, 1)
                            }

                            when (tvSeriesEpisodesUIState) {
                                is Resource.Loading -> {
                                    CircularProgressIndicator(modifier = modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(align = Alignment.CenterHorizontally))
                                }
                                is Resource.Success -> {
                                    LazyRow(modifier = modifier.fillMaxWidth(),
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                        items(count = tvSeriesEpisodesUIState.data.episodes?.size ?: 0) { index ->
                                            Card(onClick = { },
                                                shape = RoundedCornerShape(10),
                                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                                            ) {
                                                Card(
                                                    modifier = modifier.size(height = 100.dp, width = 140.dp),
                                                    shape = RoundedCornerShape(20)
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        AsyncImage(
                                                            model = ImageRequest.Builder(context = LocalContext.current)
                                                                .data(tvSeriesEpisodesUIState.data.episodes?.get(index)?.stillPath?.toImageUrl ?: "")
                                                                .crossfade(true)
                                                                .build(),
                                                            placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                                                            contentDescription = null,
                                                            modifier = modifier
                                                                .fillMaxSize(),
                                                            contentScale = ContentScale.Crop
                                                        )
                                                        Icon(
                                                            imageVector = Icons.Rounded.PlayCircle,
                                                            contentDescription = null,
                                                            tint = Color.White
                                                        )

                                                        Text(
                                                            text = "Episode ${tvSeriesEpisodesUIState.data.episodes?.get(index)?.episodeNumber}",
                                                            modifier = modifier
                                                                .fillMaxSize()
                                                                .wrapContentSize(align = Alignment.BottomStart)
                                                                .padding(10.dp),
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                is Resource.Failure -> { Text(text = "Failure") }
                            }
                        }

                        Column(modifier = modifier.height(200.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            TabRow(selectedTabIndex = pager.currentPage) {
                                items.forEachIndexed { index, horizontalPagerContent ->
                                    Tab(selected = pager.currentPage == index,
                                        onClick = {
                                            coroutineScope.launch {
                                                pager.animateScrollToPage(page = index)
                                            }
                                        },
                                        text = {
                                            Text(text = items[index].title)
                                        })
                                }
                            }

                            HorizontalPager(count = items.size, state = pager, modifier = modifier.fillMaxWidth()) { index ->
                                when (index) {
                                    0 -> {
                                        when(tvSeriesTrailerUiState) {
                                            is Resource.Loading -> { CircularProgressIndicator() }
                                            is Resource.Failure -> { Text(text = "Failed!") }
                                            is Resource.Success -> {
                                                LazyColumn(
                                                    modifier = modifier.fillMaxSize(),
                                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                                ) {
                                                    items(tvSeriesTrailerUiState.data) { trailer ->
                                                        TvSeriesTrailerCard(
                                                            tvSeriesTrailerWithYoutube = trailer,
                                                            onTrailerClick = onTrailerClick,
                                                            downloaderUiState = downloaderUiState,
                                                            onTrailerDownloadClick = onTrailerDownloadClick,
                                                            tvSeriesDetail = tvSeriesUIState.data)
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
                                                MovieImageCard(imageUrl = moviesFlow[index]?.posterPath ?: "", rating = moviesFlow[index]?.voteAverage?.toOneDecimal ?: "", movieId = moviesFlow[index]?.id ?: 0)
                                            }
                                        }
                                    }
                                    2 -> {
                                        CommentsCompose()
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieImageCard(modifier: Modifier = Modifier, imageUrl: String = "", rating: String = "", movieId: Int? = null) {

    val context = LocalContext.current

    Timber.tag("Card").d(movieId.toString())

    Card(shape = RoundedCornerShape(10), onClick = { DetailActivity.startActivity(context as Activity, type = IS_TYPE.Movie, id = movieId) }) {
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
private fun MovieTrailerCard(modifier: Modifier = Modifier,
                             movieTrailerWithYoutube: MovieTrailerWithYoutube = MovieTrailerWithYoutube("", "Iron man", "", "1 min 20sec"),
                             onTrailerClick: (String) -> Unit = { _ -> },
                             downloaderUiState: DownloadUiState = DownloadUiState.Default,
                             movieDetail: MoviesDetail? = null,
                             onTrailerDownloadClick: (String, Stream, Stream, MovieDownloadEntity) -> Unit = { _, _, _, _ -> },
                        ) {

    val context = LocalContext.current

    Card(onClick = {
        PlayActivity.startActivity(activity = context as Activity, videoTitle = null, filePath = null, videoId = movieTrailerWithYoutube.id, fromScreen = Screen.Detail)

    },
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(modifier = modifier
            .fillMaxWidth(),
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

            Column(modifier = modifier
                .weight(1f)
                .requiredHeight(100.dp)
                .padding(vertical = 16.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = movieTrailerWithYoutube.title ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = movieTrailerWithYoutube.duration?.toYoutubeDuration ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
private fun TvSeriesTrailerCard(modifier: Modifier = Modifier,
                             tvSeriesTrailerWithYoutube: TvSeriesTrailerWithYoutube = TvSeriesTrailerWithYoutube("", "Iron man", "", "1 min 20sec"),
                             onTrailerClick: (String) -> Unit = { _ -> },
                             downloaderUiState: DownloadUiState = DownloadUiState.Default,
                             tvSeriesDetail: TvSeriesDetail? = null,
                             onTrailerDownloadClick: (String, Stream, Stream, MovieDownloadEntity) -> Unit = { _, _, _, _ -> },
) {

    val context = LocalContext.current

    Card(onClick = {
        PlayActivity.startActivity(activity = context as Activity, videoTitle = null, filePath = null, videoId = tvSeriesTrailerWithYoutube.id, fromScreen = Screen.Detail)
    },
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(modifier = modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(modifier = modifier.size(height = 100.dp, width = 130.dp),
                shape = RoundedCornerShape(20)) {
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                        .data(tvSeriesTrailerWithYoutube.thumbnail ?: "")
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

            Column(modifier = modifier.weight(1f).requiredHeight(100.dp).padding(vertical = 16.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = tvSeriesTrailerWithYoutube.title ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = tvSeriesTrailerWithYoutube.duration?.toYoutubeDuration ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )

            }
        }
    }
}

@Preview
@Composable
fun CommentsCompose(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            Text(text = "24.6K Comments",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold)

            Text(text = "See all",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold)
        }

        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(CommentRepository.getComments().size) {
                CommentsPeopleCompose(comment = CommentRepository.getComments()[it])
            }
        }
    }
}

@Composable
private fun CommentsPeopleCompose(modifier: Modifier = Modifier, comment: Comment = Comment()) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(comment.imageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(height = 50.dp, width = 50.dp)
                    .clip(RoundedCornerShape(50)),
            )

            Text(text = comment.userName ?: "", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = modifier.weight(1f))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.Comment, contentDescription = null)
            }
        }

        Text(text = comment.comment ?: "", style = MaterialTheme.typography.bodyLarge, maxLines = 2, overflow = TextOverflow.Ellipsis)

        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /*TODO*/ }, modifier = modifier.then(modifier.size(24.dp))) {
                Icon(imageVector = Icons.Rounded.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = modifier.width(4.dp))

            Text(text = "${comment.likes ?: 0}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = modifier.width(16.dp))

            Text(text = comment.postedDate ?: "", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = modifier.width(16.dp))

            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Reply")
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