package com.devsimon.watchers.classes

import com.devsimon.watchers.screens.dashboard.WatchScreen

sealed class BottomAppBarScreen(val route: String) {

    object Watch : BottomAppBarScreen("watch")
    object Explore : BottomAppBarScreen("explore")
    object Search : BottomAppBarScreen("search")
    object Account : BottomAppBarScreen("account")

}
