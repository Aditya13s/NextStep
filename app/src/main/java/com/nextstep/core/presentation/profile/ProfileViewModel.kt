package com.nextstep.core.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextstep.core.domain.usecase.GetUserUseCase
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
 * ProfileViewModel.kt
 * ─────────────────────────────────────────────────────────
 * Manages state for the Profile screen.
 *
 * The Profile screen lets the user:
 *   • See their current goal and level
 *   • Change their goal (resets the plan)
 *   • Change their skill level
 *
 * When the user changes their goal, SaveUserUseCase is
 * called with resetPlan = true which resets currentDay to 1
 * and streak to 0 (starting fresh with the new goal).
 * ─────────────────────────────────────────────────────────
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            getUserUseCase().collect { user ->
                _state.update { current ->
                    current.copy(
                        isLoading = false,
                        currentGoal = user?.goal ?: "",
                        currentLevel = user?.level ?: "",
                        selectedGoal = user?.goal ?: "",
                        selectedLevel = user?.level ?: ""
                    )
                }
            }
        }
    }

    fun onGoalSelected(goal: String) {
        _state.update { it.copy(selectedGoal = goal) }
    }

    fun onLevelSelected(level: String) {
        _state.update { it.copy(selectedLevel = level) }
    }

    /**
     * Saves changes.  resetPlan = true if the goal changed
     * (the user is starting a brand-new journey).
     */
    fun onSave() {
        val s = _state.value
        if (s.selectedGoal.isEmpty() || s.selectedLevel.isEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val goalChanged = s.selectedGoal != s.currentGoal
            saveUserUseCase(
                goal      = s.selectedGoal,
                skill     = s.selectedGoal,
                level     = s.selectedLevel,
                resetPlan = goalChanged
            )

            _state.update {
                it.copy(
                    isSaving = false,
                    currentGoal = s.selectedGoal,
                    currentLevel = s.selectedLevel,
                    saveSuccess = true
                )
            }
        }
    }

    fun onSaveSuccessConsumed() {
        _state.update { it.copy(saveSuccess = false) }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = true,
    val currentGoal: String = "",
    val currentLevel: String = "",
    val selectedGoal: String = "",
    val selectedLevel: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val goals: List<String> = Constants.AVAILABLE_GOALS,
    val levels: List<String> = Constants.SKILL_LEVELS
)
