package com.nextstep.core.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * OnboardingScreen.kt
 * ─────────────────────────────────────────────────────────
 * The first screen the user sees on a fresh install.
 *
 * Layout (top → bottom):
 *   1. Welcome headline + subtitle
 *   2. Goal selection chips (scrollable FlowRow)
 *   3. Skill level chips
 *   4. "Let's Go!" CTA button
 *
 * UX decisions:
 *   • The CTA button is disabled until BOTH a goal AND a
 *     level are selected (prevents empty state in the app).
 *   • Chips use ElevatedFilterChip with selected state for
 *     clear visual feedback.
 *   • A LaunchedEffect watches state.done and navigates to
 *     Home as soon as the user is saved.
 * ─────────────────────────────────────────────────────────
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,                           // navigation callback from NavGraph
    viewModel: OnboardingViewModel = hiltViewModel()  // Hilt-provided ViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Navigate to home when the ViewModel emits done = true.
    // LaunchedEffect re-runs whenever [state.done] changes.
    LaunchedEffect(state.done) {
        if (state.done) onComplete()
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // ── Welcome headline ──────────────────────────
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -40 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "👋 Welcome to NextStep",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Stop overthinking. Do one thing today.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // ── Goal selection ────────────────────────────
            Text(
                text = "What do you want to improve?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            // FlowRow wraps chips into multiple lines automatically
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.goals.forEach { goal ->
                    ElevatedFilterChip(
                        selected = state.selectedGoal == goal,
                        onClick = { viewModel.onGoalSelected(goal) },
                        label = { Text(goal) },
                        colors = FilterChipDefaults.elevatedFilterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Skill level selection ─────────────────────
            Text(
                text = "Your current level?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.levels.forEach { level ->
                    ElevatedFilterChip(
                        selected = state.selectedLevel == level,
                        onClick = { viewModel.onLevelSelected(level) },
                        label = { Text(level) },
                        colors = FilterChipDefaults.elevatedFilterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(Modifier.height(48.dp))

            // ── CTA Button ────────────────────────────────
            Button(
                onClick = { viewModel.onComplete() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                // Disabled until both goal and level are selected
                enabled = state.selectedGoal.isNotEmpty() &&
                          state.selectedLevel.isNotEmpty() &&
                          !state.isLoading
            ) {
                if (state.isLoading) {
                    // Show a spinner while the user is being saved
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Let's Go! 🚀",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
