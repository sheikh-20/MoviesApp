package com.application.moviesapp.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Comment
import androidx.compose.material.icons.rounded.EmojiEmotions
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.domain.model.Comment
import com.application.moviesapp.domain.model.UserReview
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import kotlinx.coroutines.flow.flowOf

@Composable
fun CommentsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    userReviewFlow: LazyPagingItems<UserReview> = flowOf(PagingData.empty<UserReview>()).collectAsLazyPagingItems(),
) {

    val focusRequest = remember { FocusRequester() }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = modifier
        .fillMaxSize()
        .padding(top = paddingValues.calculateTopPadding(), start = 0.dp, end = 0.dp, bottom = 0.dp)) {

        LazyColumn(
            modifier = modifier.fillMaxSize().padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {

            items(userReviewFlow.itemCount) { index ->
                CommentsPeopleCompose(review = userReviewFlow[index] ?: return@items)
            }
        }


        Card(modifier = modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomCenter)) {
            Row(modifier = modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                OutlinedTextField(value = "",
                    label = { Text("Add comment...", style = MaterialTheme.typography.bodySmall) },
                    onValueChange = {  },
                    modifier = Modifier
                        .requiredHeight(45.dp)
                        .weight(1f)
                        .focusRequester(focusRequest),
                    interactionSource = interactionSource,
                    shape = RoundedCornerShape(30),
                    textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize, lineHeight = MaterialTheme.typography.bodyLarge.lineHeight),
                    trailingIcon = { Icon(imageVector = Icons.Rounded.EmojiEmotions, contentDescription = null) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                )

                FloatingActionButton(
                    modifier = modifier.size(50.dp),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                    onClick = { /*TODO*/ },
                    backgroundColor = MaterialTheme.colorScheme.primary) {
                    Icon(imageVector = Icons.Rounded.Send, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }

            }
        }
    }
}

@Composable
fun CommentsPeopleCompose(modifier: Modifier = Modifier, comment: Comment = Comment(), review: UserReview = UserReview(null, null, null, null, null, null, null)) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(review.authorDetails?.avatarPath?.toImageUrl)
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

            Text(text = review.author ?: "", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = modifier.weight(1f))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.Comment, contentDescription = null)
            }
        }

        Text(text = review.content ?: "", style = MaterialTheme.typography.bodyLarge, maxLines = 2, overflow = TextOverflow.Ellipsis)

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


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CommentsLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        CommentsScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CommentsDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        CommentsScreen()
    }
}