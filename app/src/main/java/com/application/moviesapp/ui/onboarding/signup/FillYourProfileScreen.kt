package com.application.moviesapp.ui.onboarding.signup

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.ui.accountsetup.UserProfile
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import timber.log.Timber

private const val TAG = "FillYourProfileScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillYourProfileScreen(modifier: Modifier = Modifier,
                          onContinueClick: (UserProfile) -> Unit = {  },
                          onProfileClick: (Uri) -> Unit = { _ -> },
                          profileUIState: Resource<Uri> = Resource.Loading) {

    var fullName by remember { mutableStateOf("") }
    var nickName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    var showGenderDropdown by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }

    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            Timber.tag(TAG).d(uri.toString())
            onProfileClick(uri)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {

        IconButton(onClick = { getContent.launch("image/*") }, modifier = modifier.size(200.dp)) {

            when (profileUIState) {
                is Resource.Loading -> {
                    Icon(imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = null,
                    modifier = modifier.size(200.dp))
                }

                is Resource.Failure -> {
                    Icon(imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        modifier = modifier.size(200.dp))
                }

                is Resource.Success -> {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(profileUIState.data)
                            .crossfade(true)
                            .build(),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = modifier.size(200.dp),
                    )
                }
            }
        }

        Column(modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text(text = stringResource(id = R.string.full_name)) },
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            OutlinedTextField(
                value = nickName,
                onValueChange = { nickName = it },
                label = { Text(text = stringResource(id = R.string.nick_name)) },
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(id = R.string.email)) },
                trailingIcon = { Icon(imageVector = Icons.Rounded.Email, contentDescription = null) },
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(text = stringResource(id = R.string.phone_number)) },
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Phone
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Box {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {  },
                    label = { Text(text = stringResource(id = R.string.gender)) },
                    trailingIcon = { Icon(
                        imageVector = Icons.Rounded.ArrowDropDown,
                        contentDescription = null
                    ) },
                    modifier = modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    interactionSource = interactionSource
                )

                DropdownMenu(
                    expanded = interactionSource.collectIsFocusedAsState().value,
                    onDismissRequest = {  },
                    modifier = modifier) {

                    DropdownMenuItem(
                        text = { Text(text = "Male") },
                        onClick = {
                            gender = "Male"
                            focusManager.clearFocus()
                        })
                    DropdownMenuItem(
                        text = { Text(text = "Female") },
                        onClick = {
                            gender = "Female"
                            focusManager.clearFocus()
                        })
                }
            }
        }

        Row(modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = { /*TODO*/ }, modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    ambientColor = MaterialTheme.colorScheme.outlineVariant,
                    spotColor = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(50)
                )
                .weight(1f)) {
                Text(text = stringResource(id = R.string.skip),  modifier = modifier.padding(4.dp))
            }
            Button(onClick = { onContinueClick(UserProfile(fullName, nickName, email, phoneNumber.toLongOrNull() ?: 0L, gender)) },
                modifier = modifier
                    .shadow(
                        elevation = 4.dp,
                        ambientColor = MaterialTheme.colorScheme.outlineVariant,
                        spotColor = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(50)
                    )
                    .weight(1f)) {
                Text(text = stringResource(id = R.string.continue_text),  modifier = modifier.padding(4.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FillYourProfileScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        FillYourProfileScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FillYourProfileScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        FillYourProfileScreen()
    }
}