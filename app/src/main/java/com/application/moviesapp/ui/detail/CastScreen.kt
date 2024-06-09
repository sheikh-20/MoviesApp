package com.application.moviesapp.ui.detail

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CastScreen(modifier: Modifier = Modifier,
               paddingValues: PaddingValues = PaddingValues(),
               selectedImage: Pair<String, List<String?>?> = Pair("", emptyList())) {

    val filteredImage = selectedImage.second?.indexOf(selectedImage.first)
    val pagerState = rememberPagerState { selectedImage.second?.size ?: 0 }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(page = filteredImage ?: 0)
        }
    }


    Box(modifier = modifier
        .fillMaxSize()) {

        HorizontalPager(state = pagerState) { index ->
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(selectedImage.second?.get(index)?.toImageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize(),
            )
        }

        Row(modifier = modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomCenter)
            .padding(16.dp)
            .background(color = Color(0x66FFFFFF), shape = RoundedCornerShape(50))) {
            Text(text = "${pagerState.currentPage.inc()} / ${selectedImage.second?.size}", color = Color.White, style = MaterialTheme.typography.bodySmall, modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }
    }
}

@Preview(showBackground = true , showSystemUi = true)
@Composable
private fun CastScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        CastScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CastScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        CastScreen()
    }
}