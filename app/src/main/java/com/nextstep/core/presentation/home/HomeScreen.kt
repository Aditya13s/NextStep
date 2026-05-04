package com.nextstep.core.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.nextstep.core.presentation.components.TaskCard
import com.nextstep.core.presentation.theme.Success

/**
 * HomeScreen.kt
 * ─────────────────────────────────────────────────────────
 * The main daily-task screen.  This is what the user sees
 * every day when they open the app.
 *
 * Layout (top → bottom):
 *   TopAppBar:    "Today's Step"  |  Profile icon
 *   StreakBadge:  🔥 streak count + progress bar
 *   PlanProgressBar: Day X of 30
 *   TaskCard:     Today's task title + description
 *   Button:       "Mark as Done" (disabled if already done)
 *
 * States handled:
 *   Loading  → CircularProgressIndicator
 *   No task  → "You've completed all 30 days! 🎉"
 *   Task     → Full layout above
 * ─────────────────────────────────────────────────────────
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Today's Step",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Profile navigation button
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->

        // AnimatedContent smoothly cross-fades between the
        // loading state and the content state.
        AnimatedContent(
            targetState = state.isLoading,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "homeContentAnimation",
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { isLoading ->

            if (isLoading) {
                // ── Loading state ──────────────────────────
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // ── Loaded state ───────────────────────────
                val user = state.user
                val task = state.task

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(16.dp))

                    // ── Streak badge ──────────────────────
                    if (user != null) {
                        StreakBadge(
                            streak = user.streak,
                            streakLevel = user.streakLevel
                        )
                        Spacer(Modifier.height(12.dp))

                        // ── Plan progress bar ─────────────
                        PlanProgressBar(currentDay = user.currentDay)
                        Spacer(Modifier.height(24.dp))
                    }

                    if (task != null) {
                        // ── Task card ─────────────────────
                        Text(
                            text = if (state.isTaskCompletedToday)
                                       "✅ Done for today!"
                                   else
                                       "Here's your next step:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))

                        TaskCard(
                            task = task,
                            isCompleted = state.isTaskCompletedToday
                        )
                        Spacer(Modifier.height(24.dp))

                        // ── Mark as Done button ───────────
                        Button(
                            onClick = { viewModel.onTaskComplete() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            // Disabled once task is completed or while loading
                            enabled = !state.isTaskCompletedToday &&
                                      !state.isMarkingDone,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.isTaskCompletedToday)
                                    Success
                                else
                                    MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            if (state.isMarkingDone) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    strokeWidth = 2.dp
                                )
                            } else if (state.isTaskCompletedToday) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null
                                )
                                Spacer(Modifier.padding(4.dp))
                                Text(
                                    text = "Completed! Come back tomorrow 🌟",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            } else {
                                Text(
                                    text = "Mark as Done ✓",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                    } else if (user != null) {
                        // ── Plan complete state ───────────
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "🎉",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize * 2
                                    )
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = "You've completed all 30 days!",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Incredible discipline. Visit Profile to start a new goal.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}
