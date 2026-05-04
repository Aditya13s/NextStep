package com.nextstep.core.presentation.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nextstep.core.domain.model.Task
import com.nextstep.core.presentation.theme.Success

/**
 * TaskCard.kt  (Reusable Component)
 * ─────────────────────────────────────────────────────────
 * Displays a single daily task in a Material 3 Card.
 *
 * Used on:
 *   • Home screen – the "Today's Step" card
 *
 * Props:
 *   @param task         The task domain object to display.
 *   @param isCompleted  Whether today's task is already done
 *                       (changes the card colour and icon).
 *   @param modifier     Standard Compose modifier for layout.
 *
 * Visual design:
 *   ┌─────────────────────────────────────────────┐
 *   │  DAY 5  •  ⏱ 15 min                       │
 *   │                                             │
 *   │  Do 20 jumping jacks                        │
 *   │                                             │
 *   │  A quick cardio burst. Keep your core …    │
 *   │                                     ✓ Done │
 *   └─────────────────────────────────────────────┘
 * ─────────────────────────────────────────────────────────
 */
@Composable
fun TaskCard(
    task: Task,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    // Animate the card background colour when the task is
    // marked as done: neutral → light green.
    val cardColor by animateColorAsState(
        targetValue = if (isCompleted)
            Success.copy(alpha = 0.12f)
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 600),
        label = "cardColorAnimation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // ── Header row: day number + duration ─────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Day number chip
                Text(
                    text = "Day ${task.day}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                // Duration badge  ⏱ 15 min
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Duration",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${task.duration} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Task title ────────────────────────────────
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            // ── Task description ──────────────────────────
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // ── Completed indicator ───────────────────────
            // Only shown after the user marks the task done
            if (isCompleted) {
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = Success,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Done!",
                        style = MaterialTheme.typography.labelLarge,
                        color = Success
                    )
                }
            }
        }
    }
}
