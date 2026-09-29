package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.ui.state.BambooThemeColor

@Composable
fun BambooTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeColor: BambooThemeColor = BambooThemeColor.GREEN,
    content: @Composable () -> Unit
) {
    val primary = when (themeColor) {
        BambooThemeColor.GREEN -> BambooPrimaryGreen
        BambooThemeColor.PURPLE -> PurplePrimary
        BambooThemeColor.BLUE -> BluePrimary
        BambooThemeColor.CYAN -> CyanPrimary
        BambooThemeColor.RED -> RedPrimary
        BambooThemeColor.PINK -> PinkPrimary
        BambooThemeColor.ORANGE -> OrangePrimary
    }

    val secondary = when (themeColor) {
        BambooThemeColor.GREEN -> BambooBrightGreen
        BambooThemeColor.PURPLE -> PurpleBright
        BambooThemeColor.BLUE -> BlueBright
        BambooThemeColor.CYAN -> CyanBright
        BambooThemeColor.RED -> RedBright
        BambooThemeColor.PINK -> PinkBright
        BambooThemeColor.ORANGE -> OrangeBright
    }

    val tertiary = when (themeColor) {
        BambooThemeColor.GREEN -> BambooSoftGreen
        BambooThemeColor.PURPLE -> PurpleSoft
        BambooThemeColor.BLUE -> BlueSoft
        BambooThemeColor.CYAN -> CyanSoft
        BambooThemeColor.RED -> RedSoft
        BambooThemeColor.PINK -> PinkSoft
        BambooThemeColor.ORANGE -> OrangeSoft
    }

    val darkColors = darkColorScheme(
        primary = primary,
        onPrimary = BambooBg,
        primaryContainer = BambooDarkGreen,
        onPrimaryContainer = BambooTextPrimary,
        secondary = secondary,
        onSecondary = BambooBg,
        tertiary = tertiary,
        onTertiary = BambooBg,
        background = BambooBg,
        onBackground = BambooTextPrimary,
        surface = BambooSurface,
        onSurface = BambooTextPrimary,
        surfaceVariant = BambooElevated,
        onSurfaceVariant = BambooTextSecondary,
        outline = BambooBorder,
        outlineVariant = BambooBorderHighlight
    )

    val lightColors = lightColorScheme(
        primary = primary,
        onPrimary = BambooBg,
        primaryContainer = tertiary,
        onPrimaryContainer = BambooBg,
        secondary = secondary,
        onSecondary = BambooTextPrimary,
        background = BambooBg,
        onBackground = BambooTextPrimary,
        surface = BambooSurface,
        onSurface = BambooTextPrimary,
        surfaceVariant = BambooElevated,
        onSurfaceVariant = BambooTextSecondary,
        outline = BambooBorder
    )

    val colorScheme = if (darkTheme) darkColors else lightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BambooBg.toArgb()
            window.navigationBarColor = BambooBg.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BambooTypography,
        content = content
    )
}
