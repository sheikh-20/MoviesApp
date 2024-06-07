package com.application.moviesapp.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import kotlinx.coroutines.launch

@Composable
fun CastDetailScreen(modifier: Modifier = Modifier,
                     paddingValues: PaddingValues = PaddingValues(),
                     castDetailUIState: Resource<CastDetailWithImages> = Resource.Loading
                     ) {

    val scrollState = rememberScrollState()
    val overviewScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var isViewMore by remember { mutableStateOf(true) }

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
            }
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