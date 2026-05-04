package com.nextstep.core.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Color.kt  (Presentation / Theme)
 * ─────────────────────────────────────────────────────────
 * All colour values used by the app's Material 3 theme.
 *
 * Design guidelines followed:
 *   • Primary brand colour: deep indigo (motivational, calm)
 *   • Accent: vibrant orange (energy, action CTA buttons)
 *   • Surfaces: near-white light / dark charcoal dark
 *   • Gamification colours for streak badges:
 *       Bronze, Silver, Gold
 *
 * Using a centralised Color.kt means:
 *   1. Changing the brand colour = one line change here.
 *   2. No magic hex strings scattered across composables.
 *   3. Easy to maintain dark and light variants.
 * ─────────────────────────────────────────────────────────
 */

// ── Primary palette ───────────────────────────────────────
val PrimaryIndigo    = Color(0xFF3F51B5)   // main brand colour
val PrimaryDark      = Color(0xFF283593)   // darker variant (dark theme primary)
val PrimaryLight     = Color(0xFF7986CB)   // lighter variant (light theme containers)
val OnPrimary        = Color(0xFFFFFFFF)   // text/icons on primary background

// ── Secondary palette ─────────────────────────────────────
val SecondaryOrange  = Color(0xFFFF6F00)   // CTA button (Mark as Done)
val SecondaryLight   = Color(0xFFFFAB00)   // lighter orange for containers
val OnSecondary      = Color(0xFFFFFFFF)

// ── Backgrounds ───────────────────────────────────────────
val BackgroundLight  = Color(0xFFF5F5F5)   // light mode screen background
val BackgroundDark   = Color(0xFF121212)   // dark mode screen background
val SurfaceLight     = Color(0xFFFFFFFF)   // cards, dialogs in light mode
val SurfaceDark      = Color(0xFF1E1E1E)   // cards, dialogs in dark mode

// ── Text ──────────────────────────────────────────────────
val TextPrimary      = Color(0xFF212121)   // headlines, body
val TextSecondary    = Color(0xFF757575)   // subtitles, captions
val TextOnDark       = Color(0xFFEEEEEE)

// ── Status colours ────────────────────────────────────────
val Success          = Color(0xFF4CAF50)   // streak active, task done
val Warning          = Color(0xFFFFC107)   // streak at risk
val Error            = Color(0xFFF44336)   // streak broken indicator

// ── Gamification ──────────────────────────────────────────
val BronzeColor      = Color(0xFFCD7F32)   // 1–6 day streak badge
val SilverColor      = Color(0xFFC0C0C0)   // 7–29 day streak badge
val GoldColor        = Color(0xFFFFD700)   // 30+ day streak badge
val StreakFlame      = Color(0xFFFF5722)   // streak counter fire icon

// ── Progress bar ──────────────────────────────────────────
val ProgressFill     = Color(0xFF3F51B5)   // filled portion of progress bar
val ProgressTrack    = Color(0xFFBBDEFB)   // unfilled track
