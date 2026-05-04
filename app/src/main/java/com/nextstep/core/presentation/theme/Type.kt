package com.nextstep.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Type.kt  (Presentation / Theme)
 * ─────────────────────────────────────────────────────────
 * Typography scale for the app, following Material 3
 * type system guidelines.
 *
 * We use the default system font (FontFamily.Default) which
 * is Roboto on Android.  If you want a custom font:
 *   1. Place the .ttf file in app/src/main/res/font/
 *   2. Create a FontFamily with Font(R.font.my_font)
 *   3. Replace FontFamily.Default below
 *
 * Key styles used in the app:
 *   titleLarge    → Screen headers (Home, Progress)
 *   headlineMedium → Daily task title on the task card
 *   bodyLarge     → Task description text
 *   bodyMedium    → Secondary info (duration, day number)
 *   labelLarge    → Button text
 *   labelSmall    → Streak badge label
 * ─────────────────────────────────────────────────────────
 */
val NextStepTypography = Typography(

    // Screen title – "Today's Step", "Your Progress"
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    // Task card headline – the actual task text
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),

    // Sub-headlines (section titles on progress screen)
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    // Task description, body text
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Secondary info (duration, day number, dates)
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    // Captions, helper text
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Button labels, chip labels
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Streak badge, small annotations
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
