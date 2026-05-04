package com.nextstep.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TaskEntity.kt  (Room / Data layer)
 * ─────────────────────────────────────────────────────────
 * Room table that stores the pre-defined task library.
 *
 * Tasks are seeded once on first launch by TaskSeeder and
 * never modified afterwards (they are read-only content).
 * Because tasks are static content, they don't need a
 * @ForeignKey – they are queried independently by
 * (skill, day) via TaskDao.
 *
 * The complete task library (30 days × 5 goals = 150 tasks)
 * lives in TaskSeeder.kt.
 * ─────────────────────────────────────────────────────────
 */
@Entity(tableName = "task")
data class TaskEntity(

    /**
     * Manual integer primary key.
     * Tasks are pre-defined, so we assign IDs in TaskSeeder
     * rather than letting Room auto-generate them.  This
     * makes it safe to reference specific tasks by ID if
     * needed in the future.
     */
    @PrimaryKey
    val id: Int,

    /**
     * Goal category this task belongs to.
     * e.g. "Fitness", "Coding", "Study"
     * Matches User.goal so the task engine can filter tasks.
     */
    val category: String,

    /**
     * Specific skill key used in the WHERE clause of
     * TaskDao.getTaskByDay(skill, day).
     * Mirrors category in MVP.
     */
    val skill: String,

    /**
     * Which day in the 30-day plan (1–30).
     * Together with [skill], uniquely identifies one task.
     */
    val day: Int,

    /**
     * Short, action-oriented headline shown on the Home card.
     * Max ~60 characters so it fits without truncation.
     */
    val title: String,

    /**
     * 1–3 sentence explanation of the task shown in the
     * expanded card view.
     */
    val description: String,

    /**
     * Estimated completion time in minutes.
     * Product spec: must be ≤ 30 minutes.
     */
    val duration: Int
)
