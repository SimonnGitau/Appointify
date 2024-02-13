package com.devsimon.watchers.screens

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devsimon.watchers.ui.theme.WatchersTheme

@Composable
fun SettingsScreen(nav1: NavHostController) {

    val backPressDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    WatchersTheme {
        Surface(modifier = Modifier
            .fillMaxSize()) {

            DisposableEffect(backPressDispatcher) {
                val callback = object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {

                        nav1.navigate("backToDash"){
                            popUpTo("toFavourites"){inclusive = true}
                            popUpTo("watch") { inclusive = true }
                        }
                    }
                }

                backPressDispatcher?.addCallback(callback)

                onDispose {
                    callback.remove()
                }
            }
        }
    }

}
