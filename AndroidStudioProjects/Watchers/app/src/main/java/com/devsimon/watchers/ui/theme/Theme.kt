package com.devsimon.watchers.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

//private val DarkColorScheme = darkColorScheme(
//    primary = Color(0xFF1C1C1C), // Dark background
//    secondary = Color(0xFF333333), // Darker background
//    tertiary = Color(0xFFFF6D40), // Dark variant of deep orange (customize as needed)
//
//    background = Color(0xFF1C1B1F), // Dark background color
//    surface = Color(0xFF1C1B1F), // Dark surface color
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFFFFFBFE), // Text color on dark background
//    onSurface = Color(0xFFFFFBFE), // Text color on dark surface
//
//    error = Color(0xFFB00020),
//)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF880E4F), // Dark Red
    secondary = Color(0xFFD32F2F), // Light Red
    tertiary = Color(0xFFEF5350), // Very Light Red (customize as needed)

    background = Color(0xFF1C1B1F), // Dark red background color
    surface = Color(0xFF1C1B1F), // Dark red surface color
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFFFFBFE), // Text color on dark background
    onSurface = Color(0xFFFFFBFE), // Text color on dark surface

    error = Color(0xFFB00020),
)


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFD32F2F), // Dark Red
    secondary = Color(0xFFE57373), // Light Red
    tertiary = Color(0xFFEF9A9A), // Very Light Red (customize as needed)

    background = Color(0xFFFFEBEE), // Light red background color
    surface = Color(0xFFFFCDD2), // Light red surface color
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFF333333), // Text color on light background
    onSurface = Color(0xFF333333), // Text color on light surface

    error = Color(0xFFB00020),
)


@Composable
fun WatchersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
      SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.primary.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
      }
    }

    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
}