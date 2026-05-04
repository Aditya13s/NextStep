package com.nextstep.core.domain.usecase

import com.nextstep.core.domain.repository.ProgressEntry
import com.nextstep.core.domain.repository.TaskRepository
import com.nextstep.core.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * GetProgressUseCase.kt
 * ─────────────────────────────────────────────────────────
 * USE CASE: Observe the user's task completion history.
 *
 * Powers the Progress screen which shows:
 *   • A list of completed tasks with dates
 *   • Total tasks completed count
 *
 * The Flow approach means the list updates in real time
 * after a new task is marked complete on the Home screen,
 * without any manual refresh logic in the ViewModel.
 * ─────────────────────────────────────────────────────────
 */
class GetProgressUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    /**
     * Returns a Flow of progress entries, newest first.
     * Each entry contains the task title and completion date
     * (see [ProgressEntry] in TaskRepository).
     */
    operator fun invoke(): Flow<List<ProgressEntry>> =
        repository.observeProgress(Constants.DEFAULT_USER_ID)
}
