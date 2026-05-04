package com.nextstep.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ProgressEntity.kt  (Room / Data layer)
 * ─────────────────────────────────────────────────────────
 * Stores one record per completed task per user per day.
 *
 * This table is the source of truth for:
 *   1. The progress history list on the Progress screen.
 *   2. The total tasks completed count.
 *   3. Detecting whether today's task has already been done
 *      (by checking if a row exists with today's date).
 *
 * There is an intentional one-to-many relationship between
 * User and Progress (one user → many progress records), but
 * we don't declare a @ForeignKey in MVP to keep the schema
 * simple and avoid cascade-delete surprises.
 * ─────────────────────────────────────────────────────────
 */
@Entity(tableName = "progress")
data class ProgressEntity(

    /**
     * Auto-generated primary key.
     * Room increments this integer for every new row.
     * The default value of 0 tells Room to use autoGenerate.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * References User.id – always "local_user" in MVP.
     * Used as a WHERE filter in ProgressDao queries so the
     * schema is ready for a multi-user future.
     */
    val userId: String,

    /**
     * References Task.id – the specific task that was completed.
     * Used to JOIN with the task table and retrieve the task
     * title for the history list.
     */
    val taskId: Int,

    /**
     * Whether the task was completed (always true when inserted
     * via CompleteTaskUseCase).  Reserved for future use where
     * a user could manually skip a task.
     */
    val completed: Boolean,

    /**
     * Midnight-aligned Unix timestamp (ms) of the day this
     * progress was recorded.
     * Using midnight ensures that "did I complete a task TODAY"
     * queries work correctly regardless of the exact time.
     * See DateUtils.todayMidnightMillis().
     */
    val date: Long
)
