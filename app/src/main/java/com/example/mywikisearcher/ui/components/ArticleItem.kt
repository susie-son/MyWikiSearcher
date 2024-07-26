package com.example.mywikisearcher.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mywikisearcher.util.Constants.COORDINATE_DECIMAL_PLACES
import com.example.mywikisearcher.R
import com.example.mywikisearcher.util.format
import com.example.mywikisearcher.util.getWikiPageUrl
import com.example.mywikisearcher.model.ArticleState
import com.example.mywikisearcher.ui.components.ArticleListItemType.OneLine
import com.example.mywikisearcher.ui.components.ArticleListItemType.ThreeLines
import com.example.mywikisearcher.ui.components.ArticleListItemType.TwoLines

@Composable
fun ArticleItem(
    article: ArticleState,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listItemType = getListItemType(article)

    ListItem(
        headlineContent = {
            Text(article.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        supportingContent = {
            article.description?.let {
                Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        },
        overlineContent = {
            article.coordinate?.let {
                Text(formatCoordinate(it))
            }
        },
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
            BookmarkButton(isBookmarked = article.bookmarked, onClick = onBookmarkClick)
        },
        modifier = modifier
            .height(listItemType.height.dp)
            .clickable {
                val openWikiPageIntent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(getWikiPageUrl(article.title))
                ).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                }
                context.startActivity(openWikiPageIntent)
            })
}

private enum class ArticleListItemType(val height: Int) {
    OneLine(56), TwoLines(72), ThreeLines(88)
}

private fun getListItemType(article: ArticleState): ArticleListItemType {
    return when {
        article.description != null && article.coordinate != null -> ThreeLines
        article.description != null -> TwoLines
        else -> OneLine
    }
}

@Composable
private fun formatCoordinate(coordinate: ArticleState.Coordinate): String {
    return stringResource(
        R.string.coordinate_format,
        coordinate.latitude.format(COORDINATE_DECIMAL_PLACES),
        coordinate.longitude.format(COORDINATE_DECIMAL_PLACES)
    )
}
