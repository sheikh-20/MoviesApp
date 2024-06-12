package com.application.moviesapp.ui.home.tvseriesnowplaying

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.application.moviesapp.R
import com.application.moviesapp.ui.viewmodel.ExploreViewModel
import com.application.moviesapp.ui.viewmodel.HomeViewModel
import com.application.moviesapp.ui.viewmodel.MovieNewReleaseUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingSeriesApp(modifier: Modifier = Modifier,
                        homeViewModel: HomeViewModel = hiltViewModel(),
                        exploreViewModel: ExploreViewModel = viewModel()
                   ) {

    val uiState: MovieNewReleaseUiState by homeViewModel.moviesNewReleaseUiState.collectAsState()
    val tvSeriesNewReleasePagingFlow = homeViewModel.nowPlayingSeriesPagingFlow().collectAsLazyPagingItems()

    val newReleaseScrollState = rememberLazyGridState()
    val newReleaseHideTopAppBar by remember(newReleaseScrollState) {
        derivedStateOf {
            newReleaseScrollState.firstVisibleItemIndex == 0
        }
    }

    val searchUiState by exploreViewModel.searchInputUiState.collectAsState()
    val tvSeriesSearchFlowState = exploreViewModel.getTvSeriesBySearch(searchUiState.search).collectAsLazyPagingItems()


    Scaffold(
        topBar = { NewReleasesTopAppbar(newReleaseHideTopAppBar, exploreViewModel, search = searchUiState.search) },
        containerColor = Color.Transparent
    ) { paddingValues ->
        NowPlayingSeriesScreen(
            modifier = modifier,
            uiState = uiState,
            tvSeriesFlow = tvSeriesNewReleasePagingFlow,
            lazyGridState = newReleaseScrollState,
            bottomPadding = paddingValues,
            tvSeriesSearchFlow = tvSeriesSearchFlowState,
            searchClicked = searchUiState.clicked)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewReleasesTopAppbar(newReleaseHideTopAppBar: Boolean,
                                 exploreViewModel: ExploreViewModel = viewModel(),
                                 search: String = "",) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    var onSearchClick by remember { mutableStateOf(false) }

    val focusRequest = remember { FocusRequester() }

    if (!interactionSource.collectIsFocusedAsState().value) {
        onSearchClick = false
    }

    if (search.isNotEmpty()) {
        exploreViewModel.updateClickInput(true)
    } else {
        exploreViewModel.updateClickInput(false)
    }


    AnimatedVisibility(
        visible = newReleaseHideTopAppBar,
        enter = slideInVertically(animationSpec = tween(durationMillis = 200)),
        exit = slideOutVertically(animationSpec = tween(durationMillis = 200))) {

        TopAppBar(
            title = {
                if (!onSearchClick) {
                    Text(text = stringResource(id = R.string.now_playing_series), fontWeight = FontWeight.SemiBold)
                } else {
                    OutlinedTextField(value = search,
                        onValueChange = exploreViewModel::updateSearchField,
                        modifier = Modifier
                            .height(64.dp)
                            .fillMaxWidth()
                            .padding(start = 0.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                            .focusRequester(focusRequest),
                        interactionSource = interactionSource,
                        shape = RoundedCornerShape(30),
                        textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize, lineHeight = MaterialTheme.typography.bodyLarge.lineHeight),
                        trailingIcon = { Icon(imageVector = Icons.Rounded.Search, contentDescription = null) },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                    )

                    SideEffect {
                        focusRequest.requestFocus()
                    }
                }
            },
            navigationIcon = {
                if (!onSearchClick) {
                    IconButton(onClick = { (context as Activity).finish() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null)
                    }
                }
            },
            actions = {
                if (!onSearchClick) {
                    IconButton(onClick = {
                        onSearchClick = true
                    }) {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                    }
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
        )
    }
}