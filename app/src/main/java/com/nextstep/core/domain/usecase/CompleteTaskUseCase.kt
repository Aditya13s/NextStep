package com.nextstep.core.domain.usecase

import com.nextstep.core.domain.model.User
import com.nextstep.core.domain.repository.TaskRepository
import com.nextstep.core.utils.DateUtils
import javax.inject.Inject

/**
 * CompleteTaskUseCase.kt
 * ─────────────────────────────────────────────────────────
 * USE CASE: Mark today's task as done and update the streak.
 *
 * Business rules applied here:
 *   1. Insert a progress record with today's date.
 *   2. Recalculate the streak:
 *        - If the user completed yesterday's task → streak++
 *        - If this is their first completion ever → streak = 1
 *        - If they missed one or more days → streak = 1 (reset)
 *   3. Advance currentDay by 1 (so tomorrow a new task appears).
 *   4. Persist the updated User.
 *
 * Guard: if the user has ALREADY completed today's task,
 * the use case exits early without duplicating the record.
 * ─────────────────────────────────────────────────────────
 */
class CompleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(user: User) {
        // Guard: don't record a completion twice on the same day
        if (user.hasCompletedToday()) return

        val nowMillis = DateUtils.nowMillis()

        // ── Streak calculation ────────────────────────────
        // If the user completed a task yesterday the streak
        // continues; otherwise it resets to 1.
        val newStreak = when {
            user.lastCompletedDate == 0L -> 1          // first ever task
            DateUtils.isYesterday(user.lastCompletedDate) -> user.streak + 1 // continuing streak
            else -> 1                                  // missed ≥ 1 day → reset
        }

        // ── Record progress in progress table ─────────────
        // markTaskCompleted writes a ProgressEntity row and
        // updates the User row (streak, currentDay, date) in
        // a single database transaction inside the repository.
        repository.markTaskCompleted(user.id, user.currentDay)

        // ── Persist updated user ───────────────────────────
        // We build the updated domain object and save it.
        // currentDay advances so tomorrow a new task is shown.
        val updatedUser = user.copy(
            streak = newStreak,
            currentDay = user.currentDay + 1,
            lastCompletedDate = nowMillis
        )
        repository.saveUser(updatedUser)
    }
}
