package com.example.mywikisearcher.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywikisearcher.R
import com.example.mywikisearcher.ui.components.ArticleList

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val articleList by viewModel.articleList.collectAsState(initial = emptyList())

    val searchText by rememberSaveable { viewModel.searchText }

    Column(modifier = modifier.fillMaxWidth()) {
        HomeTabRow(selectedTab = selectedTab, onSelectTab = viewModel::selectTab)
        if (selectedTab == HomeTab.Search) {
            OutlinedTextField(
                value = searchText,
                onValueChange = viewModel::changeSearchText,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }
        ArticleList(
            list = articleList,
            onBookmarkClick = viewModel::handleBookmark,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTabRow(
    selectedTab: HomeTab,
    onSelectTab: (HomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    SecondaryTabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = modifier
    ) {
        Tab(
            selected = selectedTab.ordinal == 0,
            onClick = { onSelectTab(HomeTab.Search) },
            text = {
                Text(stringResource(R.string.tab_search_caption))
            }
        )
        Tab(
            selected = selectedTab.ordinal == 1,
            onClick = { onSelectTab(HomeTab.Bookmarks) },
            text = {
                Text(stringResource(R.string.tab_bookmarks_caption))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.app_name))
        },
        modifier = modifier
    )
}

enum class HomeTab {
    Search,
    Bookmarks
}
