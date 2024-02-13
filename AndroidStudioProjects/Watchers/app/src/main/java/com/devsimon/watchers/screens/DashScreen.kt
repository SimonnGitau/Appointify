package com.devsimon.watchers.screens

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devsimon.watchers.classes.BottomNavigationItem
import com.devsimon.watchers.screens.dashboard.AccountScreen
import com.devsimon.watchers.screens.dashboard.ExploreScreen
import com.devsimon.watchers.screens.dashboard.SearchScreen
import com.devsimon.watchers.screens.dashboard.WatchScreen
import com.devsimon.watchers.ui.theme.WatchersTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.devsimon.watchers.classes.BottomAppBarScreen

@Composable
fun DashScreen() {
    val nav1 = rememberNavController()
    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    var showTopBar by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by nav1.currentBackStackEntryAsState()

    showBottomBar = when (navBackStackEntry?.destination?.route) {
        BottomAppBarScreen.Watch.route -> true // on this screen top bar should be shown
        BottomAppBarScreen.Explore.route -> true // here too
        BottomAppBarScreen.Search.route -> true // here too
        BottomAppBarScreen.Account.route -> true // here too
        else -> false // hide bottom bar in all other cases
    }

    WatchersTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(bottomBar = {
                if (showBottomBar) {
                    BottomNav(nav1)
                }
            }) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    // Switch screens respective to the item index

                    NavHost(nav1, startDestination = "watch") {
                        composable("watch") { WatchScreen(nav1) }
                        composable("explore") { ExploreScreen() }
                        composable("search") { SearchScreen() }
                        composable("account") { AccountScreen() }
                        composable("toFavourites") { SettingsScreen(nav1) }
                        composable("backToDash") { DashScreen() }
                    }

                }
            }
        }
    }
}


@Composable
fun BottomNav(nav1: NavHostController) {
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    val context = LocalContext.current

    when (calculateWindowSizeClass(context)) {
        WindowWidthSizeClass.Compact -> CompactScreen(nav1)
        WindowWidthSizeClass.Medium -> MediumScreen(nav1)
//        WindowWidthSizeClass.Expanded -> ExpandedScreen()
    }

    if (isPortrait) {
        CompactScreen(nav1)
    } else {
        MediumScreen(nav1)
    }
}

@Composable
fun CompactScreen(nav1: NavHostController) {
    NavigationBar {
        val navBackStackEntry by nav1.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val context = LocalContext.current

        val bottomNavItems = listOf(
            BottomNavigationItem("Watch", Icons.Filled.Home, Icons.Outlined.Home, false),
            BottomNavigationItem(
                "Explore",
                Icons.Filled.Menu,
                Icons.Outlined.Menu,
                false,
                badgeCount = 45
            ),
            BottomNavigationItem("Search", Icons.Filled.Search, Icons.Outlined.Search, false),
            BottomNavigationItem(
                "Account",
                Icons.Filled.AccountCircle,
                Icons.Outlined.AccountCircle,
                true
            )
        )

        bottomNavItems.forEach { screen ->
            val isSelected =
                currentDestination?.hierarchy?.any { it.route == screen.title.lowercase() } == true

            NavigationBarItem(
                icon = {
                    if (screen.title != "Account") {
                        BadgedBox(
                            badge = {
                                if (screen.badgeCount != null || screen.hasNews) {
                                    Badge {
                                        if (screen.badgeCount != null) {
                                            Text(text = screen.badgeCount.toString())
                                        }
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = screen.selectedIcon,
                                modifier = Modifier.size(26.dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                    } else {
                        //Show the user image

                        BadgedBox(
                            badge = {
                                if (screen.badgeCount != null || screen.hasNews) {
                                    Badge {
                                        if (screen.badgeCount != null) {
                                            Text(text = screen.badgeCount.toString())
                                        }
                                    }
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data("https://images.pexels.com/photos/1729993/pexels-photo-1729993.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2.jpg")
                                        .crossfade(true)
                                        .build(),
                                    modifier = Modifier
                                        .size(24.dp)
                                        .fillMaxSize(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                },
                selected = isSelected,
                onClick = {
                    nav1.navigate(screen.title.lowercase()) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(nav1.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Composable
fun MediumScreen(nav1: NavHostController) {
    NavigationRail {
        val navBackStackEntry by nav1.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val context = LocalContext.current

        val bottomNavItems = listOf(
            BottomNavigationItem("Watch", Icons.Filled.Home, Icons.Outlined.Home, false),
            BottomNavigationItem(
                "Explore",
                Icons.Filled.Menu,
                Icons.Outlined.Menu,
                false,
                badgeCount = 45
            ),
            BottomNavigationItem("Search", Icons.Filled.Search, Icons.Outlined.Search, false),
            BottomNavigationItem(
                "Account",
                Icons.Filled.AccountCircle,
                Icons.Outlined.AccountCircle,
                true
            )
        )

        bottomNavItems.forEach { screen ->
            val isSelected =
                currentDestination?.hierarchy?.any { it.route == screen.title.lowercase() } == true

            NavigationRailItem(
                icon = {
                    if (screen.title != "Account") {
                        BadgedBox(
                            badge = {
                                if (screen.badgeCount != null || screen.hasNews) {
                                    Badge {
                                        if (screen.badgeCount != null) {
                                            Text(text = screen.badgeCount.toString())
                                        }
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = screen.selectedIcon,
                                modifier = Modifier.size(26.dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                    } else {
                        //Show the user image

                        BadgedBox(
                            badge = {
                                if (screen.badgeCount != null || screen.hasNews) {
                                    Badge {
                                        if (screen.badgeCount != null) {
                                            Text(text = screen.badgeCount.toString())
                                        }
                                    }
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data("https://images.pexels.com/photos/1729993/pexels-photo-1729993.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2.jpg")
                                        .crossfade(true)
                                        .build(),
                                    modifier = Modifier
                                        .size(24.dp)
                                        .fillMaxSize(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                },
                selected = isSelected,
                onClick = {
                    nav1.navigate(screen.title.lowercase()) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(nav1.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
            )
        }
    }
}

//@Composable
//fun ExpandedScreen() {
//    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)
//    val selectedItem = remember { mutableStateOf(items[0]) }
//    PermanentNavigationDrawer(
//        drawerContent = {
//            PermanentDrawerSheet(Modifier.width(240.dp)) {
//                Spacer(Modifier.height(12.dp))
//                items.forEach { item ->
//                    NavigationDrawerItem(
//                        icon = { Icon(item, contentDescription = null) },
//                        label = { Text(item.name) },
//                        selected = item == selectedItem.value,
//                        onClick = {
//                            selectedItem.value = item
//                        },
//                        modifier = Modifier.padding(horizontal = 12.dp)
//                    )
//                }
//            }
//        },
//        content = {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(text = "Application content")
//            }
//        }
//    )
//}


fun calculateWindowSizeClass(context: Context): WindowWidthSizeClass {
    val configuration = context.resources.configuration
    val density = context.resources.displayMetrics.density

    val screenWidthDp = (configuration.screenWidthDp / density).toInt()

    return when {
        screenWidthDp < 600 -> WindowWidthSizeClass.Compact
        screenWidthDp < 1200 -> WindowWidthSizeClass.Medium
        else -> WindowWidthSizeClass.Expanded
    }
}
