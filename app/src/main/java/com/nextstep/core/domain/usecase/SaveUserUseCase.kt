package com.nextstep.core.domain.usecase

import com.nextstep.core.domain.model.User
import com.nextstep.core.domain.repository.TaskRepository
import com.nextstep.core.utils.Constants
import javax.inject.Inject

/**
 * SaveUserUseCase.kt
 * ─────────────────────────────────────────────────────────
 * USE CASE: Persist a new or updated User.
 *
 * Called:
 *   1. After onboarding completes – saves the new user with
 *      the chosen goal/skill/level.
 *   2. From the profile screen when the user changes their
 *      goal (resets currentDay to 1 so they start the new
 *      plan from the beginning).
 *
 * Business rule: when the goal changes, currentDay is reset
 * to 1 and the streak resets to 0, because the user is
 * starting a completely new journey.
 * ─────────────────────────────────────────────────────────
 */
class SaveUserUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    /**
     * Creates or updates the default local user.
     *
     * @param goal      The selected goal category string.
     * @param skill     Mirrors [goal] for MVP (same value).
     * @param level     "Beginner" | "Intermediate" | "Advanced"
     * @param resetPlan If true, currentDay and streak are reset
     *                  to 1 / 0 (used when changing goals).
     */
    suspend operator fun invoke(
        goal: String,
        skill: String,
        level: String,
        resetPlan: Boolean = false
    ) {
        // Fetch existing user (null on first onboarding)
        val existing = repository.getUser(Constants.DEFAULT_USER_ID)

        val user = User(
            id = Constants.DEFAULT_USER_ID,
            goal = goal,
            skill = skill,
            level = level,
            // Start/reset at day 1 when creating or resetting
            currentDay = if (resetPlan || existing == null) 1 else existing.currentDay,
            // Preserve streak unless the plan is reset
            streak = if (resetPlan) 0 else existing?.streak ?: 0,
            lastCompletedDate = if (resetPlan) 0L else existing?.lastCompletedDate ?: 0L
        )

        repository.saveUser(user)
    }
}
