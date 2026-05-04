package com.nextstep.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nextstep.core.data.local.entity.ProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * ProgressDao.kt
 * ─────────────────────────────────────────────────────────
 * Data Access Object for the "progress" table.
 *
 * The most interesting query here is the JOIN with the task
 * table to retrieve task titles alongside completion dates.
 * Room supports JOINs by returning a data class whose field
 * names match the selected column aliases.
 * ─────────────────────────────────────────────────────────
 */
@Dao
interface ProgressDao {

    /**
     * Records a completed task.
     *
     * OnConflictStrategy.IGNORE prevents duplicate entries if
     * the user somehow triggers the "Mark as Done" button
     * twice before the UI disables it.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(progress: ProgressEntity)

    /**
     * Returns a live Flow of progress rows joined with their
     * corresponding task titles, ordered newest first.
     *
     * The JOIN avoids storing the task title redundantly in the
     * progress table – we look it up from the canonical task row.
     *
     * Returns a list of [ProgressWithTitle], a lightweight
     * data class defined below that Room maps the result into.
     */
    @Query("""
        SELECT p.id, p.userId, p.taskId, p.completed, p.date,
               t.title AS taskTitle, t.day AS taskDay
        FROM progress p
        INNER JOIN task t ON p.taskId = t.id
        WHERE p.userId = :userId
        ORDER BY p.date DESC
    """)
    fun observeProgressWithTitles(userId: String): Flow<List<ProgressWithTitle>>

    /**
     * Total number of completed tasks for a user.
     * Shown in the stats card on the Progress screen.
     */
    @Query("SELECT COUNT(*) FROM progress WHERE userId = :userId AND completed = 1")
    suspend fun countCompleted(userId: String): Int
}

/**
 * ProgressWithTitle
 * ─────────────────────────────────────────────────────────
 * Room maps the result of the JOIN query above into this
 * class automatically using the column aliases in the SQL.
 *
 * This is NOT an @Entity – it's a plain data class that
 * exists only as a query result projection.
 * ─────────────────────────────────────────────────────────
 */
data class ProgressWithTitle(
    val id: Int,
    val userId: String,
    val taskId: Int,
    val completed: Boolean,
    val date: Long,
    /** Mapped from `t.title AS taskTitle` in the query */
    val taskTitle: String,
    /** Mapped from `t.day AS taskDay` in the query */
    val taskDay: Int
)
