package com.application.moviesapp.ui.download

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.moviesapp.R
import com.application.moviesapp.domain.model.SettingsPreference
import com.application.moviesapp.ui.onboarding.OnboardingActivity
import com.application.moviesapp.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun DownloadApp(modifier: Modifier = Modifier, profileViewModel: ProfileViewModel = hiltViewModel()) {

    val wifiRequiredUIState by profileViewModel.isWifiRequired.collectAsState(initial = SettingsPreference(true))

    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(BottomSheet.Default) }

    when (showBottomSheet) {
        BottomSheet.Default -> {}
        BottomSheet.DeleteDownloads -> {
            BottomSheet(
                onDismiss = { showBottomSheet = BottomSheet.Default },
                onNegativeClick = { showBottomSheet = BottomSheet.Default },
                onPositiveClick = {   },
                contentSheet = { onNegativeClick, onPositiveClick ->

                    BottomSheetDeleteDownloadsContent(
                        onNegativeClick = onNegativeClick,
                        onPositiveClick = onPositiveClick
                    )
                })
        }
        BottomSheet.DeleteCache -> {
            BottomSheet(
                onDismiss = { showBottomSheet = BottomSheet.Default },
                onNegativeClick = { showBottomSheet = BottomSheet.Default },
                onPositiveClick = {  },
                contentSheet = { onNegativeClick, onPositiveClick ->

                    BottomSheetDeleteCacheContent(
                        onNegativeClick = onNegativeClick,
                        onPositiveClick = onPositiveClick
                    )
                })
        }
    }

    Scaffold(
        topBar = { DownloadTopAppbar() }
    ) { paddingValues ->
        DownloadScreen(
            paddingValues = paddingValues,
            wifiRequired = wifiRequiredUIState,
            updateWifiPreference = profileViewModel::updateWifiPreference,
            onDeleteDownloads = { showBottomSheet = BottomSheet.DeleteDownloads },
            onDeleteCache = { showBottomSheet = BottomSheet.DeleteCache })
    }
}

enum class BottomSheet {
    Default,
    DeleteDownloads,
    DeleteCache
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BottomSheetDeleteDownloadsContent(modifier: Modifier = Modifier, onNegativeClick: () -> Unit = { }, onPositiveClick: () -> Unit = {}) {
    Column(modifier = modifier
        .padding(16.dp)
        .systemBarsPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Delete All Downloads",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold)

        Divider()

        Text(text = "Are you sure you want to delete all downloads?",
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
                Text(text = "Yes")
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BottomSheetDeleteCacheContent(modifier: Modifier = Modifier, onNegativeClick: () -> Unit = { }, onPositiveClick: () -> Unit = {}) {
    Column(modifier = modifier
        .padding(16.dp)
        .systemBarsPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Delete cache",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold)

        Divider()

        Text(text = "Are you sure you want to delete cache?",
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
                Text(text = "Yes")
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DownloadTopAppbar(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    TopAppBar(
        title = { Text(text = "Download") },
        navigationIcon = {
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
}