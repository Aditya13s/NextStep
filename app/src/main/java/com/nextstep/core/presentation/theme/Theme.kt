package com.nextstep.core.presentation.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Theme.kt  (Presentation / Theme)
 * ─────────────────────────────────────────────────────────
 * Defines the app's Material 3 colour scheme and wires it
 * to Compose's MaterialTheme.
 *
 * Material 3 has a rich colour role system.  We only need to
 * fill in the roles used in our app; the rest take on
 * sensible defaults.  Key roles we set:
 *
 *   primary         → main brand colour (buttons, active states)
 *   onPrimary       → text/icons placed ON a primary surface
 *   secondary       → CTA accent (Mark as Done button)
 *   background      → screen background
 *   surface         → card backgrounds
 *   error           → error states
 *
 * Dynamic Color:
 *   On Android 12+ (API 31+) Material You lets the system
 *   extract colours from the user's wallpaper.  We support
 *   this as an optional enhancement while keeping our brand
 *   colours as fallback on older devices.
 * ─────────────────────────────────────────────────────────
 */

// ── Light colour scheme ───────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary          = PrimaryIndigo,
    onPrimary        = OnPrimary,
    primaryContainer = PrimaryLight,
    secondary        = SecondaryOrange,
    onSecondary      = OnSecondary,
    secondaryContainer = SecondaryLight,
    background       = BackgroundLight,
    onBackground     = TextPrimary,
    surface          = SurfaceLight,
    onSurface        = TextPrimary,
    surfaceVariant   = BackgroundLight,
    onSurfaceVariant = TextSecondary,
    error            = Error,
)

// ── Dark colour scheme ────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary          = PrimaryLight,
    onPrimary        = PrimaryDark,
    primaryContainer = PrimaryDark,
    secondary        = SecondaryLight,
    onSecondary      = OnSecondary,
    secondaryContainer = SecondaryOrange,
    background       = BackgroundDark,
    onBackground     = TextOnDark,
    surface          = SurfaceDark,
    onSurface        = TextOnDark,
    surfaceVariant   = SurfaceDark,
    onSurfaceVariant = TextSecondary,
    error            = Error,
)

/**
 * NextStepTheme
 * ─────────────────────────────────────────────────────────
 * The root theme composable.  Wrap the entire app with this
 * in MainActivity to apply the colour scheme and typography
 * to every child composable.
 *
 * @param darkTheme      Follow system dark mode by default.
 * @param dynamicColor   Use Material You (Android 12+ only).
 * @param content        The composable tree to theme.
 * ─────────────────────────────────────────────────────────
 */
@Composable
fun NextStepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Choose the colour scheme:
    //   Dynamic  → system wallpaper colours (Android 12+)
    //   Dark     → our dark colour scheme
    //   Light    → our light colour scheme
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    // ── Status bar colour ─────────────────────────────────
    // SideEffect runs after each successful recomposition.
    // We use it to set the status bar colour to match the
    // app's background colour for an immersive look.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            // Light/dark icons on the status bar depending on theme
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    // Apply Material 3 theme with our colour scheme and typography
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = NextStepTypography,
        content     = content
    )
}
