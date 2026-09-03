package com.norman.newsfeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.norman.newsfeed.navigation.NewsFeedNavHost
import com.norman.newsfeed.ui.theme.NewsFeedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            NewsFeedTheme {
                NewsFeedNavHost()
            }
        }
    }
}
