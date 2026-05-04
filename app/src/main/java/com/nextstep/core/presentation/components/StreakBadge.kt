package com.nextstep.core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nextstep.core.presentation.theme.BronzeColor
import com.nextstep.core.presentation.theme.GoldColor
import com.nextstep.core.presentation.theme.SilverColor
import com.nextstep.core.presentation.theme.StreakFlame

/**
 * StreakBadge.kt  (Reusable Component)
 * ─────────────────────────────────────────────────────────
 * Displays the user's current streak with:
 *   • A fire icon (animated when active)
 *   • Day count
 *   • Badge level (Bronze / Silver / Gold)
 *   • A progress bar showing progress toward the next level
 *
 * Visual design:
 *   ┌──────────────────────────────┐
 *   │  🔥  5 Day Streak           │
 *   │  Bronze 🥉                  │
 *   │  ██████░░░░░░  6 days to Silver │
 *   └──────────────────────────────┘
 *
 * Used on:
 *   • Home screen streak counter
 * ─────────────────────────────────────────────────────────
 */
@Composable
fun StreakBadge(
    streak: Int,
    streakLevel: String,
    modifier: Modifier = Modifier
) {
    // ── Progress to next badge level ──────────────────────
    // Bronze: 1–6, Silver: 7–29, Gold: 30+
    val (progressFraction, daysToNext, nextLevel) = when {
        streak == 0  -> Triple(0f, 1, "Bronze")
        streak < 7   -> Triple(streak / 7f, 7 - streak, "Silver")
        streak < 30  -> Triple((streak - 7) / 23f, 30 - streak, "Gold")
        else         -> Triple(1f, 0, "Gold") // already at max
    }

    // Animate the progress bar fill so it smoothly grows when
    // the streak increments.
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 800),
        label = "streakProgressAnimation"
    )

    // Determine badge colour based on level string
    val badgeColor = when {
        streakLevel.contains("Gold")   -> GoldColor
        streakLevel.contains("Silver") -> SilverColor
        streakLevel.contains("Bronze") -> BronzeColor
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Top row: fire icon + streak count ─────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak fire",
                        tint = StreakFlame,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (streak == 0) "No streak yet"
                               else "$streak Day Streak",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                // Badge level chip
                Text(
                    text = streakLevel,
                    style = MaterialTheme.typography.labelLarge,
                    color = badgeColor
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Progress bar ──────────────────────────────
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = badgeColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(Modifier.height(6.dp))

            // ── Progress label ────────────────────────────
            if (daysToNext > 0) {
                Text(
                    text = "$daysToNext more days to $nextLevel",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "🏆 Maximum level reached!",
                    style = MaterialTheme.typography.bodySmall,
                    color = GoldColor
                )
            }
        }
    }
}
