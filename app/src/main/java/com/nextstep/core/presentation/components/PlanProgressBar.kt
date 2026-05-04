package com.nextstep.core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * PlanProgressBar.kt  (Reusable Component)
 * ─────────────────────────────────────────────────────────
 * A labelled progress bar showing how far through the 30-day
 * plan the user is.
 *
 * Visual design:
 *   Day 12 of 30
 *   ████████████░░░░░░░░░░  40%
 *
 * Used on:
 *   • Home screen (below the streak badge)
 *   • Progress screen (top stats card)
 *
 * @param currentDay   User's current day (1-based; day 1 = 0% done)
 * @param totalDays    Total days in the plan (30)
 * @param modifier     Compose layout modifier
 * ─────────────────────────────────────────────────────────
 */
@Composable
fun PlanProgressBar(
    currentDay: Int,
    totalDays: Int = 30,
    modifier: Modifier = Modifier
) {
    // completedDays = how many tasks the user has FINISHED
    // currentDay is 1-indexed and points to the NEXT task to do.
    // So completed = currentDay - 1.
    val completedDays = (currentDay - 1).coerceIn(0, totalDays)
    val fraction = completedDays.toFloat() / totalDays.toFloat()

    // Animate progress bar fill when the fraction changes
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(durationMillis = 700),
        label = "planProgressAnimation"
    )

    Column(modifier = modifier) {

        // ── Labels row ────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Day $currentDay of $totalDays",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${(fraction * 100).toInt()}% complete",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(6.dp))

        // ── Progress bar ──────────────────────────────────
        LinearProgressIndicator(
            progress = { animatedFraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
