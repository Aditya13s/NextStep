package com.nextstep.core.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextstep.core.domain.repository.ProgressEntry
import com.nextstep.core.domain.usecase.GetProgressUseCase
import com.nextstep.core.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ProgressViewModel.kt
 * ─────────────────────────────────────────────────────────
 * Manages state for the Progress screen.
 *
 * Combines two Flows:
 *   1. User Flow → current streak, currentDay
 *   2. Progress Flow → list of completed task entries
 *
 * Using Flow.combine() means a single state update fires
 * whenever EITHER source emits, keeping the UI always
 * in sync with the latest data from Room.
 * ─────────────────────────────────────────────────────────
 */
@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getProgressUseCase: GetProgressUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            // combine() emits a new value whenever either
            // userFlow OR progressFlow emits.
            getUserUseCase()
                .combine(getProgressUseCase()) { user, entries ->
                    ProgressUiState(
                        isLoading = false,
                        streak = user?.streak ?: 0,
                        streakLevel = user?.streakLevel ?: "–",
                        currentDay = user?.currentDay ?: 1,
                        goal = user?.goal ?: "",
                        totalCompleted = entries.size,
                        entries = entries
                    )
                }
                .collect { state ->
                    _state.update { state }
                }
        }
    }
}

/**
 * ProgressUiState
 * ─────────────────────────────────────────────────────────
 * All data needed by the Progress screen in one snapshot.
 * ─────────────────────────────────────────────────────────
 */
data class ProgressUiState(
    val isLoading: Boolean = true,
    val streak: Int = 0,
    val streakLevel: String = "–",
    val currentDay: Int = 1,
    val goal: String = "",
    val totalCompleted: Int = 0,
    val entries: List<ProgressEntry> = emptyList()
)
