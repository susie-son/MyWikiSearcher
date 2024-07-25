package com.example.mywikisearcher.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mywikisearcher.ui.home.HomeScreen
import com.example.mywikisearcher.ui.home.HomeTopBar
import com.example.mywikisearcher.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { HomeTopBar() }
                ) { innerPadding ->
                    HomeScreen(Modifier.fillMaxSize().padding(innerPadding))
                }
            }
        }
    }
}
