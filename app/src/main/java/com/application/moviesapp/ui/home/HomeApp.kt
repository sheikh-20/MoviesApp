package com.application.moviesapp.ui.home

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.application.moviesapp.R
import com.application.moviesapp.ui.home.download.DownloadScreen
import com.application.moviesapp.ui.home.explore.ExploreScreen
import com.application.moviesapp.ui.home.mylist.MyListScreen
import com.application.moviesapp.ui.home.profile.ProfileScreen
import com.application.moviesapp.ui.viewmodel.ExploreUiState
import com.application.moviesapp.ui.viewmodel.ExploreViewModel
import com.application.moviesapp.ui.viewmodel.HomeViewModel
import com.application.moviesapp.ui.viewmodel.MoviesWithNewReleaseUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeApp(modifier: Modifier = Modifier,
            navController: NavHostController = rememberNavController(),
            homeViewModel: HomeViewModel = hiltViewModel(),
            exploreViewModel: ExploreViewModel = hiltViewModel()) {

    val homeUiState: MoviesWithNewReleaseUiState by homeViewModel.moviesWithNewReleaseUiState.collectAsState()
    val exploreUiState: ExploreUiState by exploreViewModel.exploreUiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState()

//    ModalBottomSheet(
//        onDismissRequest = {  },
//        sheetState = modalSheetState,
//
//        ) {
//        SortFilterContent()
//    }

    Scaffold(
        topBar = { HomeTopAppbar(navController, exploreViewModel) },
        bottomBar = { HomeBottomBarNavigation(navController) },
        containerColor = Color.Transparent
    ) { paddingValues ->
        NavHost(modifier = modifier.padding(paddingValues),
            navController = navController, startDestination = BottomNavigationScreens.Home.route) {
            composable(route = BottomNavigationScreens.Home.route) {
                HomeScreen(modifier = modifier, uiState = homeUiState)
            }
            composable(route = BottomNavigationScreens.Explore.route) {
                ExploreScreen(uiState = exploreUiState)
            }
            composable(route = BottomNavigationScreens.MyList.route) {
                MyListScreen()
            }
            composable(route = BottomNavigationScreens.Download.route) {
                DownloadScreen()
            }
            composable(route = BottomNavigationScreens.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppbar(navController: NavHostController, exploreViewModel: ExploreViewModel = viewModel()) {

    val context = LocalContext.current

    when (navController.currentBackStackEntryAsState().value?.destination?.route) {
        BottomNavigationScreens.Explore.route -> {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = exploreViewModel.searchInputField,
                        onValueChange = exploreViewModel::updateSearchField,
                        label = {  
                            Text(text = "Search")
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                        },
                        modifier = Modifier
                            .height(64.dp)
                            .fillMaxWidth()
                            .padding(end = 20.dp),
                        shape = RoundedCornerShape(30)
                        )
                        },
                actions = {
                    FloatingActionButton(onClick = { Toast.makeText(context, "Clicked!", Toast.LENGTH_LONG).show() }, elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(defaultElevation = 0.dp)) {
                        Icon(imageVector = Icons.Outlined.Tune, contentDescription = null)
                    }
                },
            )
        }
        BottomNavigationScreens.MyList.route -> {
            TopAppBar(
                title = { Text(text = "My List") },
                navigationIcon = {
                    IconButton(onClick = {   }) {
                        Icon(painter = painterResource(id = R.drawable.ic_movie),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary)
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
        BottomNavigationScreens.Download.route -> {
            TopAppBar(
                title = { Text(text = "Download") },
                navigationIcon = {
                    IconButton(onClick = {   }) {
                        Icon(painter = painterResource(id = R.drawable.ic_movie),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary)
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
        BottomNavigationScreens.Profile.route -> {
            TopAppBar(
                title = { Text(text = "Profile") },
                navigationIcon = {
                    IconButton(onClick = {   }) {
                        Icon(painter = painterResource(id = R.drawable.ic_movie),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
            )
        }
        else -> {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {   }) {
                        Icon(painter = painterResource(id = R.drawable.ic_movie),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                    }

                    IconButton(onClick = { NotificationActivity.startActivity(context as Activity) }) {
                        Icon(imageVector = Icons.Rounded.NotificationsNone, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
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

    NavigationBar {
        navigationBarItems.forEach { 
            NavigationBarItem(
                selected = navController.currentBackStackEntryAsState().value?.destination?.route == it.route,
                onClick = {
                    navController.popBackStack()
                    navController.navigate(it.route)
                          },
                icon = { Icon(imageVector = it.vectorResource, contentDescription = null) },
                label = { Text(text = stringResource(id = it.stringResource)) })
        }
    }
}

@Composable
private fun SortFilterContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
        ) {

        Text(text = "Sort & Filter",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)

        HorizontalDivider()

        Text(text = "Categories",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)

        Text(text = "Regions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        
        Text(text = "Genre",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        
        Text(text = "Time/Periods",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)

        Text(text = "Sort",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)

        HorizontalDivider()

        Row(modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { }, modifier = modifier.weight(1f)) {
                Text(text = "Reset")
            }
            Button(onClick = { }, modifier = modifier.weight(1f)) {
                Text(text = "Apply")
            }
        }
    }
}

sealed class BottomNavigationScreens(val route: String, @StringRes val stringResource: Int, val vectorResource: ImageVector) {
    object Home: BottomNavigationScreens(route = "Home", stringResource = R.string.home, vectorResource = Icons.Rounded.Home)
    object Explore: BottomNavigationScreens(route = "Explore", stringResource = R.string.explore, vectorResource = Icons.Rounded.Explore)
    object MyList: BottomNavigationScreens(route = "MyList", stringResource = R.string.mylist, vectorResource = Icons.Rounded.Bookmark)
    object Download: BottomNavigationScreens(route = "Download", stringResource = R.string.download, vectorResource = Icons.Outlined.FileDownload)
    object Profile: BottomNavigationScreens(route = "Profile", stringResource = R.string.profile, vectorResource = Icons.Rounded.Person)
}