package com.example.shoppinglist.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PinkDark,
    onPrimary = PinkOn,
    primaryContainer = PinkDarkPrimaryContainer,
    onPrimaryContainer = PinkDarkOnPrimaryContainer,
    secondary = PinkSecondary,
    secondaryContainer = PinkDarkSecondaryContainer,
    onSecondaryContainer = PinkDarkOnSecondaryContainer,
    background = PinkDarkBackground,
    onBackground = PinkDarkOnBackground,
    surface = PinkSurfaceDark,
    onSurface = PinkOnSurfaceDark
)


private val LightColorScheme = lightColorScheme(
    primary = PinkLight,
    onPrimary = PinkOn,
    primaryContainer = PinkPrimaryContainer,
    onPrimaryContainer = PinkOnPrimaryContainer,
    secondary = PinkSecondary,
    secondaryContainer = PinkSecondaryContainer,
    onSecondaryContainer = PinkOnSecondaryContainer,
    background = PinkBackground,
    onBackground = PinkOnBackground,
    surface = PinkSurface,
    onSurface = PinkOnSurface
)


@Composable
fun ShoppingListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
