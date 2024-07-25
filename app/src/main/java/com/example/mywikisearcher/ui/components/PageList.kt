package com.example.mywikisearcher.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.example.mywikisearcher.model.QueryResponse

@Composable
fun PageList(
    list: List<QueryResponse.Query.Page>,
    onBookmarkClick: (QueryResponse.Query.Page) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(list) { page ->
            PageItem(
                page = page,
                onBookmarkClick = { onBookmarkClick(page) }
            )
        }
    }
}

@Composable
fun PageItem(
    page: QueryResponse.Query.Page,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // TODO: Add content descriptions
    ListItem(
        headlineContent = { page.title?.let { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) } },
        supportingContent = { page.description?.let { Text(it, maxLines = 2, overflow = TextOverflow.Ellipsis) } },
        leadingContent = {
            AsyncImage(
                model = page.thumbnail?.source,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Crop
            )
        },
        trailingContent = {
            // TODO: Handle UI
            BookmarkButton(isBookmarked = true, onClick = onBookmarkClick)
        },
        modifier = modifier.clickable {
            page.title?.let {
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
            contentDescription = null
        )
    }
}
