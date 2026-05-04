package com.nextstep.core.domain.model

/**
 * User.kt  (Domain model)
 * ─────────────────────────────────────────────────────────
 * This is the DOMAIN representation of a user.
 *
 * Domain models are pure Kotlin data classes that the rest
 * of the app (ViewModels, use cases, UI) works with.  They
 * are deliberately separate from the Room Entity classes in
 * the data layer for two reasons:
 *
 *   1. If the database schema changes, we only update the
 *      Entity and the repository mapper – the domain model
 *      and all callers remain untouched.
 *
 *   2. Domain models can contain computed fields (like
 *      [streakLevel]) that don't exist as database columns.
 *
 * Mapping: UserEntity → User is done in TaskRepositoryImpl.
 * ─────────────────────────────────────────────────────────
 */
data class User(

    /** Unique identifier – "local_user" for the single local account. */
    val id: String,

    /**
     * The user's chosen goal category (e.g. "Fitness", "Coding").
     * This determines which subset of tasks is shown to them.
     */
    val goal: String,

    /**
     * Skill within the chosen goal (currently maps 1:1 with
     * the goal in MVP – used to filter tasks in Room query).
     */
    val skill: String,

    /**
     * Self-reported skill level: "Beginner", "Intermediate",
     * or "Advanced".  Will be used in Phase 2 to surface
     * difficulty-appropriate tasks.
     */
    val level: String,

    /**
     * Which task day the user is currently on (1-based).
     * Increments by 1 after each task completion.
     * The task engine fetches tasks by (skill, day).
     */
    val currentDay: Int,

    /**
     * Number of consecutive days the user has completed a task.
     * Resets to 0 if they miss a day.
     */
    val streak: Int,

    /**
     * Unix timestamp (ms) of when the user last completed a
     * task.  Used to determine whether the streak is still
     * active (completed yesterday) or broken (missed a day).
     * 0L means the user has never completed a task.
     */
    val lastCompletedDate: Long
) {
    /**
     * Computed property that converts the raw streak count
     * into a displayable badge level.
     *
     *  0       → "–"      (not yet started)
     *  1–6     → "Bronze"
     *  7–29    → "Silver"
     *  30+     → "Gold"
     *
     * This logic lives in the domain model (not the ViewModel
     * or UI) because it is a business rule: the thresholds
     * could change and we want one canonical location.
     */
    val streakLevel: String
        get() = when {
            streak <= 0  -> "–"
            streak < 7   -> "Bronze 🥉"
            streak < 30  -> "Silver 🥈"
            else         -> "Gold 🥇"
        }

    /**
     * Returns true when the user has already completed
     * today's task (no action needed until tomorrow).
     */
    fun hasCompletedToday(): Boolean {
        if (lastCompletedDate == 0L) return false
        return com.nextstep.core.utils.DateUtils.isToday(lastCompletedDate)
    }
}
