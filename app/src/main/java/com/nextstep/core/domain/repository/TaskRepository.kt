package com.nextstep.core.domain.repository

import com.nextstep.core.domain.model.Task
import com.nextstep.core.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * TaskRepository.kt  (Domain interface)
 * ─────────────────────────────────────────────────────────
 * This INTERFACE defines what the domain layer NEEDS from
 * the data layer.  It intentionally knows nothing about
 * Room, SQLite, or any other storage technology.
 *
 * Why an interface instead of a concrete class?
 *   1. The domain layer stays completely free of Android /
 *      library dependencies.
 *   2. The concrete implementation (TaskRepositoryImpl) can
 *      be swapped out – e.g. replace Room with an in-memory
 *      fake for unit tests, or with a Firebase backend later.
 *   3. Hilt's @Binds annotation wires the interface to the
 *      implementation at compile time with zero runtime cost.
 *
 * Rule: every method here models a BUSINESS ACTION, not a
 * database query.  Use plain English names.
 * ─────────────────────────────────────────────────────────
 */
interface TaskRepository {

    // ── User operations ───────────────────────────────────

    /**
     * Emits the current user as a Flow so the UI automatically
     * recomposes if the user data changes (streak, currentDay…).
     * Returns null when no user has been created yet
     * (i.e. onboarding has not been completed).
     */
    fun observeUser(userId: String): Flow<User?>

    /**
     * One-shot read of the user – used in use cases that only
     * need a snapshot (not a live stream).
     */
    suspend fun getUser(userId: String): User?

    /**
     * Persists a new user after onboarding, or updates an
     * existing one (e.g. after goal change in profile screen).
     */
    suspend fun saveUser(user: User)

    // ── Task operations ───────────────────────────────────

    /**
     * Returns the single task for [skill] on [day].
     * Called by GetTodayTaskUseCase every time the home
     * screen is opened.
     *
     * Returns null if the task database hasn't been seeded
     * yet or if the user has completed all 30 days.
     */
    suspend fun getTaskByDay(skill: String, day: Int): Task?

    /**
     * Returns true when the task table already contains data
     * for [category].  Used by the seeder to avoid
     * re-inserting tasks on every launch.
     */
    suspend fun hasTasksForCategory(category: String): Boolean

    /**
     * Bulk-inserts a list of pre-defined tasks into the DB.
     * Called once by TaskSeeder on first launch.
     */
    suspend fun insertTasks(tasks: List<Task>)

    // ── Progress / streak operations ──────────────────────

    /**
     * Records that [userId] completed the task for [day].
     * Also responsible for updating the streak counter and
     * the lastCompletedDate on the User row.
     */
    suspend fun markTaskCompleted(userId: String, day: Int)

    /**
     * Returns a Flow of all progress entries for [userId],
     * newest first.  Powers the Progress screen history list.
     */
    fun observeProgress(userId: String): Flow<List<ProgressEntry>>

    /**
     * Returns the total number of tasks completed by [userId].
     * Used for the statistics card on the Progress screen.
     */
    suspend fun getTotalCompleted(userId: String): Int
}

/**
 * Lightweight projection used by the Progress screen.
 * Avoids exposing the full ProgressEntity / Room JOIN result
 * to the domain / presentation layers.
 */
data class ProgressEntry(
    val taskTitle: String,
    val completedDate: Long,
    val day: Int
)
