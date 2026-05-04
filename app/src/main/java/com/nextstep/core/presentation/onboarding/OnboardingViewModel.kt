package com.nextstep.core.presentation.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextstep.core.domain.usecase.SaveUserUseCase
import com.nextstep.core.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * OnboardingViewModel.kt
 * ─────────────────────────────────────────────────────────
 * Manages the UI state for the onboarding flow.
 *
 * The onboarding flow has two steps:
 *   Step 1 → Goal selection (Fitness, Coding, Study…)
 *   Step 2 → Skill level selection (Beginner / Intermediate / Advanced)
 *
 * After both steps, the user taps "Let's Go!" which calls
 * saveUser() to persist the profile and navigate to Home.
 *
 * @HiltViewModel – Hilt creates this ViewModel and injects
 * its dependencies.  The ViewModel survives configuration
 * changes (rotation) because it lives in the ViewModelStore,
 * not the Composable.
 * ─────────────────────────────────────────────────────────
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
    private val dataStore: DataStore<Preferences>   // persists the "onboarding done" flag
) : ViewModel() {

    // ── State ─────────────────────────────────────────────
    // A single data class holds ALL UI state.  This avoids
    // the "multiple MutableStateFlow" anti-pattern and makes
    // the state easy to snapshot in unit tests.
    private val _state = MutableStateFlow(OnboardingUiState())
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    // ── Events ────────────────────────────────────────────

    /** Called when the user taps a goal chip */
    fun onGoalSelected(goal: String) {
        _state.update { it.copy(selectedGoal = goal) }
    }

    /** Called when the user taps a level chip */
    fun onLevelSelected(level: String) {
        _state.update { it.copy(selectedLevel = level) }
    }

    /**
     * Called when the user taps "Let's Go!".
     * Saves the user to Room and emits [OnboardingUiState.done]
     * = true so the NavGraph can navigate to the Home screen.
     */
    fun onComplete() {
        val currentState = _state.value

        // Guard: both fields must be filled (UI should enforce
        // this, but we double-check here for safety)
        if (currentState.selectedGoal.isEmpty() ||
            currentState.selectedLevel.isEmpty()) return

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            saveUserUseCase(
                goal  = currentState.selectedGoal,
                skill = currentState.selectedGoal,  // mirrors goal in MVP
                level = currentState.selectedLevel
            )
            // Persist the onboarding-complete flag so MainActivity
            // navigates directly to Home on subsequent launches.
            dataStore.edit { prefs ->
                prefs[booleanPreferencesKey(Constants.KEY_ONBOARDING_DONE)] = true
            }
            _state.update { it.copy(isLoading = false, done = true) }
        }
    }
}

/**
 * OnboardingUiState
 * ─────────────────────────────────────────────────────────
 * Immutable snapshot of the onboarding screen's state.
 * The ViewModel emits a new copy of this whenever something
 * changes (StateFlow + data class = automatic UI updates).
 * ─────────────────────────────────────────────────────────
 */
data class OnboardingUiState(
    /** The goal the user has tapped (empty = none selected) */
    val selectedGoal: String = "",
    /** The skill level the user has tapped */
    val selectedLevel: String = "",
    /** True while the SaveUserUseCase coroutine is running */
    val isLoading: Boolean = false,
    /** True after the user is saved – triggers navigation to Home */
    val done: Boolean = false,
    /** Available goals from Constants */
    val goals: List<String> = Constants.AVAILABLE_GOALS,
    /** Available skill levels from Constants */
    val levels: List<String> = Constants.SKILL_LEVELS
)
