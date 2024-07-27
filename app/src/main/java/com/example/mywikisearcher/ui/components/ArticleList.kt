package com.example.mywikisearcher.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mywikisearcher.model.ArticleState

@Composable
fun ArticleList(
    articles: List<ArticleState>,
    onBookmarkClick: (ArticleState) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(articles) { index, article ->
            if (index == articles.lastIndex) {
                onLoadMore()
            }
            ArticleItem(article = article, onBookmarkClick = { onBookmarkClick(article) })
        }
    }
}
