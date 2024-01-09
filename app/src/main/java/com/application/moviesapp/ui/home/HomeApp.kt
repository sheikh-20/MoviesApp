package com.application.moviesapp.ui.home

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.UserPreferences
import com.application.moviesapp.data.SORT_BY
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.data.local.entity.MovieDownloadEntity
import com.application.moviesapp.domain.model.LanguagePreference
import com.application.moviesapp.domain.model.MovieGenre
import com.application.moviesapp.domain.model.SettingsPreference
import com.application.moviesapp.ui.home.download.DownloadScreen
import com.application.moviesapp.ui.home.explore.ExploreScreen
import com.application.moviesapp.ui.home.mylist.MyListScreen
import com.application.moviesapp.ui.home.profile.ProfileScreen
import com.application.moviesapp.ui.language.language
import com.application.moviesapp.ui.onboarding.OnboardingActivity
import com.application.moviesapp.ui.utility.SetLanguage
import com.application.moviesapp.ui.utility.getFileSize
import com.application.moviesapp.ui.viewmodel.DownloadViewModel
import com.application.moviesapp.ui.viewmodel.ExploreUiState
import com.application.moviesapp.ui.viewmodel.ExploreViewModel
import com.application.moviesapp.ui.viewmodel.HomeViewModel
import com.application.moviesapp.ui.viewmodel.MyListViewModel
import com.application.moviesapp.ui.viewmodel.OnboardingViewModel
import com.application.moviesapp.ui.viewmodel.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import java.io.File


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeApp(modifier: Modifier = Modifier,
            navController: NavHostController = rememberNavController(),
            homeViewModel: HomeViewModel = hiltViewModel(),
            exploreViewModel: ExploreViewModel = hiltViewModel(),
            myListViewModel: MyListViewModel = hiltViewModel(),
            profileViewModel: ProfileViewModel = hiltViewModel(),
            downloadViewModel: DownloadViewModel = hiltViewModel(),
            onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {

    val searchUiState by exploreViewModel.searchInputUiState.collectAsState()
    val sortAndFilterUiState by exploreViewModel.sortAndFilterUiState.collectAsState()

    val movieWithTvSeriesUiState by homeViewModel.movieWithTvSeriesUiState.collectAsState()

    val exploreUiState: ExploreUiState by exploreViewModel.exploreUiState.collectAsState()
    val profileUiState by homeViewModel.profileInfoUiState.collectAsState()
    val darkModeUiState by profileViewModel.isDarkMode.collectAsState(initial = SettingsPreference(false))


    var searchValue by remember {
        mutableStateOf("")
    }
    val downloadUiState by downloadViewModel.readAllDownload(searchValue).collectAsState()

    val genreUiState by exploreViewModel.genreUiState.collectAsState()
    val accountSetupUiCase by exploreViewModel.readUserPreference.collectAsState()

    val moviesFlowState = exploreViewModel.moviesPagingFlow(
        genres = sortAndFilterUiState.genre,
        sortBy = sortAndFilterUiState.sortBy,
        includeAdult = sortAndFilterUiState.includeAdult
    ).collectAsLazyPagingItems()

    val moviesSearchFlowState = exploreViewModel.getMovieBySearch(searchUiState.search).collectAsLazyPagingItems()

    val myListFlowState = myListViewModel.getMovieFavouritePagingFlow.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(BottomSheet.Default) }

    var downloadEntity by remember { mutableStateOf(MovieDownloadEntity(backdropPath = "", title = "", runtime = "", filePath = "")) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val permissionState = rememberMultiplePermissionsState(permissions = listOf(Manifest.permission.POST_NOTIFICATIONS))

    val profileUIState by onboardingViewModel.profilePhotoUIState.collectAsState()

    val languageUIState by profileViewModel.selectLanguage.collectAsState(initial = LanguagePreference(
        language[0].language[0])
    )

    SetLanguage(languageUIState.language)

    permissionState.permissions.forEach {
        when (it.permission) {
            Manifest.permission.POST_NOTIFICATIONS -> {
                when {
                    it.hasPermission -> {
//                                        coroutineScope.launch {
//                                            snackbarHostState.showSnackbar(message = "Notification permission granted!", duration = SnackbarDuration.Short)
//                                        }
                    }
                    it.shouldShowRationale -> {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(message = "Notification permission is needed", duration = SnackbarDuration.Long)
                        }
                    }
                    it.hasPermission.not() && it.shouldShowRationale.not() -> {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(message = "Notification permission was permanently denied, You can enable it on app settings!", duration = SnackbarDuration.Long)
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        permissionState.launchMultiplePermissionRequest()
        onboardingViewModel.getProfilePhoto()
    }

    when (showBottomSheet) {
        BottomSheet.Default -> { }
        BottomSheet.Filter -> {
            BottomSheet(
                onDismiss = { showBottomSheet = BottomSheet.Default },
                onNegativeClick = { showBottomSheet = BottomSheet.Default },
                onPositiveClick = { showBottomSheet = BottomSheet.Default },
                contentSheet = {
                    onNegativeClick, onPositiveClick ->

                    BottomSheetFilterContent(
                        onNegativeClick = onNegativeClick,
                        onPositiveClick = {
                                genres, sortBy, includeAdult ->
                            exploreViewModel.setSortAndFilter(genre = genres, sortBy = sortBy, includeAdult = includeAdult)
                            onPositiveClick()
                                          },
                        onFilterCalled = { exploreViewModel.getGenre(it) },
                        uiState = genreUiState,
                        readUserPreferences = accountSetupUiCase,
                        onCategoryClick = { exploreViewModel.getGenre(it) },
                        onGenreUpdate = { exploreViewModel.updateGenre(it ?: return@BottomSheetFilterContent) }
                    )
                })
        }
        BottomSheet.Logout -> {
            BottomSheet(
                onDismiss = { showBottomSheet = BottomSheet.Default },
                onNegativeClick = { showBottomSheet = BottomSheet.Default },
                onPositiveClick = {
                    homeViewModel.signOut()
                    (context as Activity).finish()
                    OnboardingActivity.startActivity(context as Activity)
                },
                contentSheet = {
                        onNegativeClick, onPositiveClick ->

                    BottomSheetContent(
                        onNegativeClick = onNegativeClick,
                        onPositiveClick = onPositiveClick
                    )
                })
        }
        BottomSheet.DownloadDelete -> {
            BottomSheet(
                onDismiss = { showBottomSheet = BottomSheet.Default },
                onNegativeClick = { showBottomSheet = BottomSheet.Default },
                onPositiveClick = {
                    coroutineScope.launch {
                        downloadViewModel.deleteMovieDownload(downloadEntity)
                    }
                    showBottomSheet = BottomSheet.Default
                },
                contentSheet = { onNegativeClick, onPositiveClick ->
                    BottomSheetContentDownloadDelete(
                        onNegativeClick = onNegativeClick,
                        onPositiveClick = onPositiveClick,
                        movieDownloadEntity = downloadEntity
                    )
                }
            )
        }
    }

    val exploreScrollState = rememberLazyGridState()
    val exploreHideTopAppBar by remember(exploreScrollState) {
        derivedStateOf {
            exploreScrollState.firstVisibleItemIndex == 0
        }
    }

    val myListScrollState = rememberLazyGridState()
    val myListHideTopAppBar by remember(myListScrollState) {
        derivedStateOf {
            myListScrollState.firstVisibleItemIndex == 0
        }
    }

    val downloadScrollState = rememberLazyListState()
    val downloadHideTopAppBar by remember(downloadScrollState) {
        derivedStateOf {
            downloadScrollState.firstVisibleItemIndex == 0
        }
    }

    Scaffold(
        topBar = {
            HomeTopAppbar(
                navController = navController,
                exploreViewModel = exploreViewModel,
                exploreHideTopAppBar = exploreHideTopAppBar,
                mylistHideTopAppBar = myListHideTopAppBar,
                downloadHideTopAppBar = downloadHideTopAppBar,
                onFilterClick = { showBottomSheet = BottomSheet.Filter },
                search = searchUiState.search,
                onHomeSearchClick = {
                    navController.popBackStack()
                    navController.navigate(BottomNavigationScreens.Explore.route)
                },
                searchValue = searchValue,
                onDownloadSearch = { searchValue = it })
        },
        bottomBar = {
            HomeBottomBarNavigation(navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        NavHost(
            navController = navController, startDestination = BottomNavigationScreens.Home.route) {
            composable(route = BottomNavigationScreens.Home.route) {
                HomeScreen(
                    modifier = modifier,
                    uiState = movieWithTvSeriesUiState,
                    bottomPadding = paddingValues,
                    goToDownloadClick = {
                        navController.popBackStack()
                        navController.navigate(BottomNavigationScreens.Download.route)
                    },
                    goToMyListClick = {
                    },
                    onMovieWithTvSeries = homeViewModel::getMovieWithTvSeries
                )
            }
            composable(route = BottomNavigationScreens.Explore.route) {
                ExploreScreen(
                    modifier = modifier,
                    uiState = exploreUiState,
                    moviesDiscoverFlow = moviesFlowState,
                    movieSearchFlow = moviesSearchFlowState,
                    lazyGridState = exploreScrollState,
                    bottomPadding = paddingValues,
                    searchClicked = searchUiState.clicked,
                    onGoToDownloadsClick = {
                        navController.popBackStack()
                        navController.navigate(BottomNavigationScreens.Download.route)
                    })
            }
            composable(route = BottomNavigationScreens.MyList.route) {
                MyListScreen(
                    modifier = modifier,
                    moviesFavouriteFlow = myListFlowState,
                    lazyGridState = myListScrollState,
                    bottomPadding = paddingValues)
            }
            composable(route = BottomNavigationScreens.Download.route) {
                DownloadScreen(
                    modifier = modifier,
                    downloadUiState = downloadUiState,
                    lazyListState = downloadScrollState,
                    bottomPadding = paddingValues,
                    onDeleteClick = {
                        showBottomSheet = BottomSheet.DownloadDelete
                        downloadEntity = it
                    }
                )
            }
            composable(route = BottomNavigationScreens.Profile.route) {
                ProfileScreen(
                    uiState = profileUiState,
                    onSignOutClick = {
                        showBottomSheet = BottomSheet.Logout
                    },
                    darkModeUiState = darkModeUiState,
                    onModeClick = profileViewModel::updateMode,
                    onProfileClick = onboardingViewModel::uploadProfilePhoto,
                    profileUIState = profileUIState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(modifier: Modifier = Modifier,
                        onDismiss: () -> Unit = {},
                        onNegativeClick: () -> Unit = {},
                        onPositiveClick: () -> Unit = {},
                        contentSheet: @Composable (onNegativeClick: () -> Unit, onPositiveClick: () -> Unit) -> Unit = { _, _ -> }
                        ) {

    val bottomSheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            coroutineScope.launch {
                onDismiss()
                bottomSheet.hide()
            }
        },
        sheetState = bottomSheet,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {

        contentSheet(onNegativeClick, onPositiveClick)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BottomSheetFilterContent(modifier: Modifier = Modifier,
                                     onNegativeClick: () -> Unit = { },
                                     onPositiveClick: (genres: List<MovieGenre.Genre>, sortBy: SORT_BY, includeAdult: Boolean) -> Unit = { _, _, _ ->  },
                                     onFilterCalled: (Categories) -> Unit = { _ -> },
                                     uiState: Resource<MovieGenre> = Resource.Loading,
                                     readUserPreferences: UserPreferences? = null,
                                     onCategoryClick: (Categories) -> Unit = { _ -> },
                                     onGenreUpdate: (MovieGenre.Genre?) -> Unit = {  _ -> }) {

    val itemsListCategories = listOf(Categories.Movies, Categories.TV)

    var selectedItemCategories by remember {
        mutableStateOf(itemsListCategories[0])
    }

    val itemsListSort = listOf(
        Pair("Popularity", SORT_BY.POPULARITY),
        Pair("Latest Release", SORT_BY.LATEST_RELEASE),
        Pair("Vote Average", SORT_BY.VOTE_AVERAGE)
    )

    var selectedItemSort by remember {
        mutableStateOf(itemsListSort[0])
    }

    var includeAdult by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = null) {
        onFilterCalled(selectedItemCategories)
    }

    when (uiState) {
        is Resource.Loading -> {
            CircularProgressIndicator(
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
            )
        }

        is Resource.Failure -> {
            Text(text = "Failed")
        }

        is Resource.Success -> {
            Column(modifier = modifier
                .padding(vertical = 16.dp)
                .systemBarsPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(text = stringResource(R.string.sort_filter),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold)

                Divider()

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    Text(text = stringResource(R.string.categories),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier.padding(horizontal = 16.dp))

                    Row(modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)) {
                        itemsListCategories.forEach { item ->
                            FilterChip(
                                modifier = modifier
                                    .requiredHeight(36.dp)
                                    .padding(horizontal = 6.dp)
                                    .weight(1f), // gap between items
                                selected = (item == selectedItemCategories),
                                onClick = {
                                    selectedItemCategories = item
                                    onCategoryClick(item)
                                          },
                                label = { Text(text = item.title, modifier = modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                                shape = RoundedCornerShape(50),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White)
                                )
                        }
                    }

                    Text(text = stringResource(R.string.genres),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier.padding(horizontal = 16.dp))


                    LazyRow(modifier = modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 16.dp)) {

                        items(uiState.data.genres ?: return@LazyRow) { item ->
                            FilterChip(
                                modifier = modifier
                                    .requiredHeight(36.dp)
                                    .padding(horizontal = 6.dp),
                                selected = readUserPreferences?.genreList?.any { it.id == item?.id } ?: false,
                                onClick = {
                                    onGenreUpdate(item)
                                          },
                                label = { Text(text = item?.name ?: "", modifier = modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                                shape = RoundedCornerShape(50),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White)
                            )
                        }
                    }

                    Text(text = stringResource(R.string.sort),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier.padding(horizontal = 16.dp))

                    LazyRow(modifier = modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 16.dp)) {
                        items(itemsListSort) { item ->
                            FilterChip(
                                modifier = modifier
                                    .requiredHeight(36.dp)
                                    .padding(horizontal = 6.dp),
                                selected = (item == selectedItemSort),
                                onClick = { selectedItemSort = item },
                                label = { Text(text = item.first, modifier = modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                                shape = RoundedCornerShape(50),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White)
                                )
                        }
                    }

                    Text(text = stringResource(R.string.include_adult),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier.padding(horizontal = 16.dp))

                    FilterChip(
                        modifier = modifier
                            .requiredHeight(36.dp)
                            .padding(horizontal = 16.dp),
                        selected = includeAdult,
                        onClick = { includeAdult = !includeAdult },
                        label = { Text(text = stringResource(R.string.no), modifier = modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White)
                    )
                }

                Divider()

                Row(modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {

                    OutlinedButton(onClick = onNegativeClick, modifier = modifier
                        .weight(1f)
                        .requiredHeight(50.dp)) {
                        Text(text = stringResource(R.string.reset))
                    }

                    Button(onClick = { onPositiveClick(readUserPreferences?.genreList?.map { MovieGenre.Genre(id = it.id, name = it.name) } ?: return@Button, selectedItemSort.second, includeAdult) }, modifier = modifier
                        .weight(1f)
                        .requiredHeight(50.dp)) {
                        Text(text = stringResource(R.string.apply))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BottomSheetContent(modifier: Modifier = Modifier, onNegativeClick: () -> Unit = { }, onPositiveClick: () -> Unit = {}) {
    Column(modifier = modifier
        .padding(16.dp)
        .systemBarsPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = stringResource(R.string.logout),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold)

        Divider()

        Text(text = stringResource(R.string.are_you_sure_you_want_to_log_out),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth())

        Row(modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            OutlinedButton(onClick = onNegativeClick, modifier = modifier
                .weight(1f)
                .requiredHeight(50.dp)) {
                Text(text = stringResource(id = R.string.cancel))
            }

            Button(onClick = onPositiveClick, modifier = modifier
                .weight(1f)
                .requiredHeight(50.dp)) {
                Text(text = stringResource(R.string.yes_logout))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BottomSheetContentDownloadDelete(modifier: Modifier = Modifier, onNegativeClick: () -> Unit = { }, onPositiveClick: () -> Unit = {}, movieDownloadEntity: MovieDownloadEntity? = null) {

    val context = LocalContext.current

    Column(modifier = modifier
        .padding(16.dp)
        .systemBarsPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = stringResource(R.string.delete),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold)

        Divider()

        Text(text = stringResource(R.string.are_you_sure_you_want_to_delete_this_downloaded_content),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth())


        Row(modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(modifier = modifier.size(height = 100.dp, width = 130.dp),
                shape = RoundedCornerShape(20)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                        .data(movieDownloadEntity?.backdropPath ?: "")
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
                .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = movieDownloadEntity?.title ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = movieDownloadEntity?.runtime.toString() ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = modifier.weight(1f))

                Card {
                    Text(text = "${movieDownloadEntity?.filePath?.getFileSize(context)} MB",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }


        Row(modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            OutlinedButton(onClick = onNegativeClick, modifier = modifier
                .weight(1f)
                .requiredHeight(50.dp)) {
                Text(text = stringResource(id = R.string.cancel))
            }

            Button(onClick = {
                File(context.filesDir, "/output/${movieDownloadEntity?.filePath}").delete()
                onPositiveClick()
            }, modifier = modifier
                .weight(1f)
                .requiredHeight(50.dp)) {
                Text(text = stringResource(R.string.yes_delete))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppbar(navController: NavHostController,
                          exploreViewModel: ExploreViewModel = viewModel(),
                          exploreHideTopAppBar: Boolean,
                          mylistHideTopAppBar: Boolean,
                          downloadHideTopAppBar: Boolean,
                          onFilterClick: () -> Unit = {},
                          search: String = "",
                          onHomeSearchClick: () -> Unit = {  },
                          searchValue: String = "",
                          onDownloadSearch: (String) -> Unit = { _ -> }
                          ) {

    val context = LocalContext.current

    when (navController.currentBackStackEntryAsState().value?.destination?.route) {
        BottomNavigationScreens.Explore.route -> {

            val focusManager = LocalFocusManager.current

            if (search.isNotEmpty()) {
                exploreViewModel.updateClickInput(true)
            } else {
                exploreViewModel.updateClickInput(false)
            }

            AnimatedVisibility(
                visible = exploreHideTopAppBar,
                enter = slideInVertically(animationSpec = tween(durationMillis = 200)),
                exit = slideOutVertically(animationSpec = tween(durationMillis = 200))) {

                TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = search,
                            onValueChange = exploreViewModel::updateSearchField,
                            label = {
                                Text(text = stringResource(R.string.search))
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                            },
                            modifier = Modifier
                                .height(64.dp)
                                .fillMaxWidth()
                                .padding(end = 20.dp),
                            shape = RoundedCornerShape(30),
                            textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize, lineHeight = MaterialTheme.typography.bodyLarge.lineHeight),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                        )
                    },
                    actions = {
                        FloatingActionButton(
                            modifier = Modifier
                                .requiredHeight(64.dp)
                                .padding(top = 8.dp, end = 4.dp),
                            onClick = onFilterClick,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(defaultElevation = 0.dp),
                            containerColor = MaterialTheme.colorScheme.primaryContainer) {
                            Icon(imageVector = Icons.Outlined.Tune, contentDescription = null, tint = MaterialTheme.colorScheme.background)
                        }
                    },
                )
            }
        }
        BottomNavigationScreens.MyList.route -> {

            AnimatedVisibility(
                visible = mylistHideTopAppBar,
                enter = slideInVertically(animationSpec = tween(durationMillis = 200)),
                exit = slideOutVertically(animationSpec = tween(durationMillis = 200))) {

                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.my_list)) },
                    navigationIcon = {
                        IconButton(onClick = {   }) {
                            Icon(painter = painterResource(id = R.drawable.ic_movie),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
                )

            }
        }
        BottomNavigationScreens.Download.route -> {

            val focusManager = LocalFocusManager.current
            val interactionSource = remember { MutableInteractionSource() }
            var onSearchClick by remember { mutableStateOf(false) }

            val focusRequest = remember { FocusRequester() }

            if (!interactionSource.collectIsFocusedAsState().value) {
                onSearchClick = false

            }

            AnimatedVisibility(
                visible = downloadHideTopAppBar,
                enter = slideInVertically(animationSpec = tween(durationMillis = 200)),
                exit = slideOutVertically(animationSpec = tween(durationMillis = 200))) {

                TopAppBar(
                    title = {
                        if (!onSearchClick) {
                            Text(text = stringResource(id = R.string.download))
                        } else {
                            OutlinedTextField(value = searchValue,
                                onValueChange = onDownloadSearch,
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
                            IconButton(onClick = {   }) {
                                Icon(painter = painterResource(id = R.drawable.ic_movie),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary)
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
        BottomNavigationScreens.Profile.route -> {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.profile)) },
                navigationIcon = {
                    IconButton(onClick = {   }) {
                        Icon(painter = painterResource(id = R.drawable.ic_movie),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
            )
        }
        BottomNavigationScreens.Home.route -> {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {   }) {
                        Icon(painter = painterResource(id = R.drawable.ic_movie),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary)
                    }
                },
                actions = {
                    IconButton(onClick = onHomeSearchClick) {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = null, tint = Color.White)
                    }

                    IconButton(onClick = { NotificationActivity.startActivity(context as Activity) }) {
                        Icon(imageVector = Icons.Rounded.NotificationsNone, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    }
}

@Composable
private fun HomeBottomBarNavigation(navController: NavHostController,
                                    homeViewModel: HomeViewModel = viewModel(),
                                    exploreViewModel: ExploreViewModel = viewModel()) {

    val navigationBarItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.Explore,
        BottomNavigationScreens.MyList,
        BottomNavigationScreens.Download,
        BottomNavigationScreens.Profile)

    NavigationBar(modifier = Modifier.clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
     containerColor = Color.Transparent,
        tonalElevation = 3.dp
        ) {
        navigationBarItems.forEach { 
            NavigationBarItem(
                selected = navController.currentBackStackEntryAsState().value?.destination?.route == it.route,
                onClick = {
                    navController.popBackStack()
                    navController.navigate(it.route)
                          },
                icon = { Icon(imageVector = it.vectorResource, contentDescription = null) },
                label = { Text(text = stringResource(id = it.stringResource)) },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color.Gray, selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = Color.Gray, selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(LocalAbsoluteTonalElevation.current)
                ),
                )
        }
    }
}

enum class BottomSheet {
    Default, Logout, Filter, DownloadDelete
}

//sealed interface BottomSheet {
//    object Default: BottomSheet
//    object Logout: BottomSheet
//    object Filter: BottomSheet
//    data class DownloadDelete(val movie: MovieDownloadEntity): BottomSheet
//}

enum class Categories(val title: String) {
    Movies("Movies"), TV("TV series")
}

sealed class BottomNavigationScreens(val route: String, @StringRes val stringResource: Int, val vectorResource: ImageVector) {
    object Home: BottomNavigationScreens(route = "Home", stringResource = R.string.home, vectorResource = Icons.Rounded.Home)
    object Explore: BottomNavigationScreens(route = "Explore", stringResource = R.string.explore, vectorResource = Icons.Rounded.Explore)
    object MyList: BottomNavigationScreens(route = "MyList", stringResource = R.string.mylist, vectorResource = Icons.Rounded.Bookmark)
    object Download: BottomNavigationScreens(route = "Download", stringResource = R.string.download, vectorResource = Icons.Rounded.FileDownload)
    object Profile: BottomNavigationScreens(route = "Profile", stringResource = R.string.profile, vectorResource = Icons.Rounded.Person)
}