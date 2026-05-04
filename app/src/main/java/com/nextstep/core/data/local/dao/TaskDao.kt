package com.nextstep.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nextstep.core.data.local.entity.TaskEntity

/**
 * TaskDao.kt
 * ─────────────────────────────────────────────────────────
 * Data Access Object for the "task" table.
 *
 * Tasks are pre-seeded read-only data.  There are no UPDATE
 * or DELETE operations – we only INSERT once (at first launch)
 * and SELECT at runtime.
 * ─────────────────────────────────────────────────────────
 */
@Dao
interface TaskDao {

    /**
     * Bulk-inserts a list of TaskEntity rows.
     *
     * OnConflictStrategy.IGNORE: if a row with the same
     * primary key already exists, the insert is silently
     * skipped.  This prevents duplicate tasks if the seeder
     * is accidentally called more than once.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    /**
     * Returns the single task for a given skill + day.
     *
     * SQL: SELECT * FROM task WHERE skill = ? AND day = ? LIMIT 1
     *
     * Returns null if:
     *   - The task table hasn't been seeded yet.
     *   - The user has progressed past day 30 (no task exists).
     *
     * The LIMIT 1 is a safety guard; (skill, day) should be
     * unique in the seeded data.
     */
    @Query("SELECT * FROM task WHERE skill = :skill AND day = :day LIMIT 1")
    suspend fun getTaskByDay(skill: String, day: Int): TaskEntity?

    /**
     * Returns the count of tasks seeded for a given category.
     *
     * Used by TaskSeeder to check whether tasks have already
     * been inserted, avoiding duplicate inserts on every launch.
     *
     * If count > 0 → seeding already done → skip.
     */
    @Query("SELECT COUNT(*) FROM task WHERE category = :category")
    suspend fun countTasksForCategory(category: String): Int
}
