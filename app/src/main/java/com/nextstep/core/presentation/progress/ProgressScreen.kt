package com.nextstep.core.presentation.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nextstep.core.presentation.components.PlanProgressBar
import com.nextstep.core.presentation.components.StreakBadge
import com.nextstep.core.presentation.theme.Success
import com.nextstep.core.utils.DateUtils

/**
 * ProgressScreen.kt
 * ─────────────────────────────────────────────────────────
 * Shows the user's overall progress stats and task history.
 *
 * Layout:
 *   TopAppBar:     "Your Progress"
 *   Stats card:    Total completed | Current streak | Day X of 30
 *   StreakBadge:   Streak with progress bar
 *   PlanProgressBar: Plan completion bar
 *   History list:  LazyColumn of completed tasks (newest first)
 * ─────────────────────────────────────────────────────────
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Progress",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->

        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        // LazyColumn handles the entire screen including the
        // sticky header cards so the stats scroll with the history.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            // ── Stats summary card ─────────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                StatsCard(
                    totalCompleted = state.totalCompleted,
                    streak = state.streak,
                    goal = state.goal
                )
                Spacer(Modifier.height(12.dp))
            }

            // ── Streak badge ───────────────────────────────
            item {
                StreakBadge(
                    streak = state.streak,
                    streakLevel = state.streakLevel
                )
                Spacer(Modifier.height(12.dp))
            }

            // ── Plan progress bar ──────────────────────────
            item {
                PlanProgressBar(currentDay = state.currentDay)
                Spacer(Modifier.height(24.dp))
            }

            // ── History section header ─────────────────────
            item {
                Text(
                    text = "Task History",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Divider()
                Spacer(Modifier.height(8.dp))
            }

            // ── History items ──────────────────────────────
            if (state.entries.isEmpty()) {
                item {
                    Text(
                        text = "No completed tasks yet.\nGo do Day 1! 💪",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            } else {
                items(
                    items = state.entries,
                    key = { it.completedDate } // stable key for list animations
                ) { entry ->
                    ProgressHistoryItem(entry)
                    Spacer(Modifier.height(8.dp))
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

/**
 * StatsCard – Three key numbers in a row
 */
@Composable
private fun StatsCard(
    totalCompleted: Int,
    streak: Int,
    goal: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem(label = "Completed", value = totalCompleted.toString())
            StatItem(label = "Streak",    value = "$streak 🔥")
            StatItem(label = "Goal",      value = goal.ifEmpty { "–" })
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

/**
 * ProgressHistoryItem – single row in the task history list
 */
@Composable
private fun ProgressHistoryItem(
    entry: com.nextstep.core.domain.repository.ProgressEntry
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Day ${entry.day}: ${entry.taskTitle}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = DateUtils.formatDate(entry.completedDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completed",
                tint = Success
            )
        }
    }
}
