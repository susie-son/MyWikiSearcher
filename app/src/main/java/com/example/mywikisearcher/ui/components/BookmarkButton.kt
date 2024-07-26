package com.example.mywikisearcher.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.mywikisearcher.R

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
