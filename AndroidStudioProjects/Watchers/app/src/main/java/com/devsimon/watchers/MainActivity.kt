package com.devsimon.watchers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devsimon.watchers.screens.DashScreen
import com.devsimon.watchers.screens.LoginScreen
import com.devsimon.watchers.screens.MotionPictureExample
import com.devsimon.watchers.ui.theme.WatchersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchersTheme {
                val navController = rememberNavController()

                //Graphing routes to navigate through different screens
                NavHost(navController = navController, startDestination = "toLogIn") {
                    composable("onboardingScreen") { MotionPictureExample(navController) }
                    composable("toLogIn") { LoginScreen(navController) }
                    composable("toDashboard") { DashScreen() }
                }
            }
        }
    }
}
