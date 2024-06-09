package com.application.moviesapp.ui.detail

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.data.api.MoviesApi
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.model.CastDetailWithImages
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.utility.toOneDecimal
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CastDetailScreen(modifier: Modifier = Modifier,
                     paddingValues: PaddingValues = PaddingValues(),
                     castDetailUIState: Resource<CastDetailWithImages> = Resource.Loading,
                     onImageClick: (Pair<String, List<String?>?>) -> Unit = { _ -> }
                     ) {

    val scrollState = rememberScrollState()
    val overviewScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var isViewMore by remember { mutableStateOf(true) }

    val items = listOf(
        HorizontalPagerContent("Images"),
        HorizontalPagerContent("Movies"),
        HorizontalPagerContent("TV Series"),
    )

    val pager = rememberPagerState()

    when (castDetailUIState) {
        is Resource.Loading -> {
            CircularProgressIndicator(modifier = modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center))
        }
        is Resource.Failure -> {
            Column(modifier = modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)) {
                androidx.compose.material3.Text(text = "Failure")
            }
        }
        is Resource.Success -> {
            Column(modifier = modifier
                .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                    .data(castDetailUIState.data.detail.profilePath?.toImageUrl ?: "")
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

                Column(modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = castDetailUIState.data.detail.name ?: "", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold,)

                    Column(modifier = modifier
                        .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "Birthday", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        Text(text = castDetailUIState.data.detail.birthday ?: "", style = MaterialTheme.typography.bodyLarge)
                    }

                    Column(modifier = modifier
                        .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "Place of Birth", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        Text(text = castDetailUIState.data.detail.placeOfBirth ?: "", style = MaterialTheme.typography.bodyLarge)
                    }

                    Column(modifier = modifier
                        .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "Biography", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

                        if ((castDetailUIState.data.detail.biography?.length ?: 0) >= 200) {
                            if (isViewMore) {
                                ClickableText(
                                    text = buildAnnotatedString {
                                        append(castDetailUIState.data.detail.biography?.take(200) ?: "")
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
                                        .fillMaxWidth(),
                                    onClick = {
                                        isViewMore = !isViewMore
                                        if (!isViewMore) {
                                            coroutineScope.launch {
                                                overviewScrollState.animateScrollTo(value = castDetailUIState.data.detail.biography?.length ?: 0)
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
                                            append(castDetailUIState.data.detail.biography ?: "")
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
                                            .fillMaxWidth(),
                                        onClick = { isViewMore = !isViewMore },
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = castDetailUIState.data.detail.biography?.length.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                            )
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

                                LazyVerticalGrid(columns = GridCells.Fixed(2),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                ) {
                                    items(castDetailUIState.data.images.profiles?.size ?: 0) { index ->
                                        CastImageCard(imageUrl = castDetailUIState.data.images.profiles?.get(index)?.filePath ?: "", onImageClick = onImageClick, imageList = castDetailUIState.data.images.profiles?.map { it?.filePath ?: ""})
                                    }
                                }
                            }
                            1 -> {
                                LazyVerticalGrid(columns = GridCells.Fixed(2),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                ) {

                                    items(castDetailUIState.data.castMovieCredits.cast?.size ?: 0) { index ->
                                        MovieImageCard(imageUrl = castDetailUIState.data.castMovieCredits.cast?.get(index)?.posterPath ?: "", rating = castDetailUIState.data.castMovieCredits.cast?.get(index)?.voteAverage?.toOneDecimal ?: "", movieId = castDetailUIState.data.castMovieCredits.cast?.get(index)?.id ?: 0)
                                    }
                                }
                            }
                            2 -> {
                                LazyVerticalGrid(columns = GridCells.Fixed(2),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                ) {

                                    items(castDetailUIState.data.castTvSeriesCredits.cast?.size ?: 0) { index ->
                                        TvSeriesImageCard(imageUrl = castDetailUIState.data.castTvSeriesCredits.cast?.get(index)?.posterPath ?: "", rating = castDetailUIState.data.castTvSeriesCredits.cast?.get(index)?.voteAverage?.toOneDecimal ?: "", tvSeriesId = castDetailUIState.data.castTvSeriesCredits.cast?.get(index)?.id ?: 0)
                                    }
                                }
                            }
                        }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TvSeriesImageCard(modifier: Modifier = Modifier, imageUrl: String = "", rating: String = "", tvSeriesId: Int? = null) {

    val context = LocalContext.current


    Card(shape = RoundedCornerShape(10), onClick = { DetailActivity.startActivity(context as Activity, type = IS_TYPE.TvSeries, id = tvSeriesId) }) {
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CastImageCard(modifier: Modifier = Modifier, imageUrl: String = "", onImageClick: (Pair<String, List<String?>?>) -> Unit = {  }, imageList: List<String?>? = emptyList()) {

    val context = LocalContext.current

    Card(shape = RoundedCornerShape(10), onClick = { onImageClick(Pair(imageUrl, imageList)) }) {
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
                modifier = modifier.fillMaxWidth().requiredHeight(200.dp),
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CastDetailLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        CastDetailScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CastDetailDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        CastDetailScreen()
    }
}