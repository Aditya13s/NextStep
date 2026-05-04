package com.nextstep.core.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextstep.core.domain.model.Task
import com.nextstep.core.domain.model.User
import com.nextstep.core.domain.usecase.CompleteTaskUseCase
import com.nextstep.core.domain.usecase.GetTodayTaskUseCase
import com.nextstep.core.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * HomeViewModel.kt
 * ─────────────────────────────────────────────────────────
 * Manages the UI state for the Home screen.
 *
 * Responsibilities:
 *   1. Observe the user (streak, currentDay, goal) via Flow.
 *   2. Fetch today's task whenever the user changes.
 *   3. Handle the "Mark as Done" action.
 *
 * Data flow:
 *   Room DB → UserDao.observeUser() → GetUserUseCase
 *           → HomeViewModel.user flow
 *           → LaunchedEffect in HomeViewModel fetches task
 *           → HomeUiState emitted → HomeScreen recomposes
 *
 * This is the MVVM pattern:
 *   View  (HomeScreen)      ← observes StateFlow
 *   Model (use cases + Room) ← provides data
 *   ViewModel               ← mediates between them
 * ─────────────────────────────────────────────────────────
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getTodayTaskUseCase: GetTodayTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase
) : ViewModel() {

    // ── State ─────────────────────────────────────────────
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        // Start observing the user immediately when the
        // ViewModel is created.  viewModelScope is cancelled
        // automatically when the ViewModel is cleared, so
        // there is no memory leak.
        observeUser()
    }

    /**
     * Collects the user Flow from GetUserUseCase.
     *
     * Every time a new User is emitted (e.g. after streak
     * update), we fetch the corresponding today-task and
     * update [_state].
     */
    private fun observeUser() {
        viewModelScope.launch {
            getUserUseCase().collect { user ->
                if (user == null) {
                    // Onboarding not completed yet
                    _state.update { it.copy(isLoading = false, user = null, task = null) }
                    return@collect
                }

                _state.update { it.copy(isLoading = true, user = user) }

                // Fetch today's task for this user's skill + day
                val task = getTodayTaskUseCase(user)
                _state.update {
                    it.copy(
                        isLoading = false,
                        task = task,
                        // User has completed today if lastCompletedDate is today
                        isTaskCompletedToday = user.hasCompletedToday()
                    )
                }
            }
        }
    }

    /**
     * Called when the user taps "Mark as Done".
     *
     * CompleteTaskUseCase:
     *   1. Guards against double-completion.
     *   2. Calculates the new streak.
     *   3. Inserts a progress record.
     *   4. Saves the updated User.
     *
     * After saving, the observeUser() collector picks up the
     * updated User and automatically refreshes the state.
     */
    fun onTaskComplete() {
        val user = _state.value.user ?: return
        viewModelScope.launch {
            _state.update { it.copy(isMarkingDone = true) }
            completeTaskUseCase(user)
            _state.update { it.copy(isMarkingDone = false) }
            // State will auto-refresh via the observeUser() Flow
        }
    }
}

/**
 * HomeUiState
 * ─────────────────────────────────────────────────────────
 * Complete snapshot of the Home screen's state.
 *
 * The screen observes this via collectAsStateWithLifecycle()
 * and recomposes when any field changes.
 * ─────────────────────────────────────────────────────────
 */
data class HomeUiState(
    /** True while initial data is being loaded */
    val isLoading: Boolean = true,
    /** The current user (null before onboarding) */
    val user: User? = null,
    /** Today's task (null if plan is complete or loading) */
    val task: Task? = null,
    /** True if the user already completed today's task */
    val isTaskCompletedToday: Boolean = false,
    /** True while the CompleteTaskUseCase is running */
    val isMarkingDone: Boolean = false
)
