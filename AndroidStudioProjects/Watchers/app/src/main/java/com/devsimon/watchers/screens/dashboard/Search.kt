package com.devsimon.watchers.screens.dashboard

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.devsimon.watchers.ui.theme.WatchersTheme

@Composable
fun SearchScreen(){
    WatchersTheme {
        Surface {
            Text(text = "Search Screen")
        }
    }
}