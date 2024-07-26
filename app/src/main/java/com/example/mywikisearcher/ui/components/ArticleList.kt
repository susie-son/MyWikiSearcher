package com.example.mywikisearcher.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mywikisearcher.R
import com.example.mywikisearcher.model.ArticleDisplayModel

@Composable
fun ArticleList(
    list: List<ArticleDisplayModel>,
    listState: LazyListState,
    onBookmarkClick: (ArticleDisplayModel) -> Unit,
    onLoadMoreResults: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        itemsIndexed(list) { index, article ->
            if (index == list.lastIndex) {
                onLoadMoreResults()
            }
            ArticleItem(
                article = article,
                onBookmarkClick = { onBookmarkClick(article) }
            )
        }
    }
}

@Composable
fun ArticleItem(
    article: ArticleDisplayModel,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    ListItem(
        headlineContent = { article.title?.let { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) } },
        supportingContent = { article.description?.let { Text(it, maxLines = 2, overflow = TextOverflow.Ellipsis) } },
        leadingContent = {
            article.thumbnail?.let {
                AsyncImage(
                    model = it,
                    // TODO: Add content description
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    contentScale = ContentScale.Crop
                )
            }
        },
        trailingContent = {
            BookmarkButton(isBookmarked = article.isBookmarked, onClick = onBookmarkClick)
        },
        modifier = modifier.clickable {
            article.title?.let {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/$it"))
                )
            }
        }
    )
}

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(
                if (isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
            ),
            // TODO: Add content description
            contentDescription = null
        )
    }
}
