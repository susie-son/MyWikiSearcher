package com.example.mywikisearcher.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywikisearcher.R
import com.example.mywikisearcher.model.ArticleState
import com.example.mywikisearcher.ui.components.ArticleList
import com.example.mywikisearcher.ui.components.SearchBar

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = viewModel()) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val articles by viewModel.articleList.collectAsState(emptyList())

    HomeScreenContent(
        selectedTab = selectedTab,
        searchText = searchText,
        articles = articles,
        onSelectTab = viewModel::onTabSelected,
        onSearchText = viewModel::onSearchTextChange,
        onBookmarkClick = viewModel::onBookmarkToggle,
        onLoadMore = viewModel::onLoadMoreResults,
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    selectedTab: HomeScreenTab,
    searchText: String,
    articles: List<ArticleState>,
    onSelectTab: (HomeScreenTab) -> Unit,
    onSearchText: (String) -> Unit,
    onBookmarkClick: (ArticleState) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HomeTabRow(selectedTab = selectedTab, onSelectTab = onSelectTab)
        if (selectedTab == HomeScreenTab.Search) {
            SearchBar(
                searchText = searchText,
                onSearchTextChanged = onSearchText,
                modifier = Modifier.fillMaxWidth()
            )
        }
        ArticleList(
            articles = articles,
            onBookmarkClick = onBookmarkClick,
            onLoadMore = onLoadMore,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun HomeTabRow(
    selectedTab: HomeScreenTab,
    onSelectTab: (HomeScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(selectedTabIndex = selectedTab.ordinal, modifier = modifier) {
        HomeScreenTab.entries.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onSelectTab(tab) },
                text = { Text(stringResource(tab.textResourceId)) }
            )
        }
    }
}

enum class HomeScreenTab(@StringRes val textResourceId: Int) {
    Search(R.string.tab_search_caption),
    Bookmarks(R.string.tab_bookmarks_caption)
}
