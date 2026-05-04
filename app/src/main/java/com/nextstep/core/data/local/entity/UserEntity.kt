package com.nextstep.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * UserEntity.kt  (Room / Data layer)
 * ─────────────────────────────────────────────────────────
 * This is the ROOM DATABASE representation of a user.
 *
 * @Entity marks this class as a Room table.  Room's KSP
 * annotation processor reads the annotations at compile time
 * and generates the SQL DDL + DAO implementation code.
 *
 * tableName = "user" → the SQLite table will be named "user".
 *
 * Why separate from the domain User model?
 *   • Room-specific annotations (@Entity, @PrimaryKey, etc.)
 *     don't belong in the domain/presentation layers.
 *   • If we later migrate to a different ORM or add Firebase
 *     sync, only this file and the repository mapper change.
 * ─────────────────────────────────────────────────────────
 */
@Entity(tableName = "user")
data class UserEntity(

    /**
     * Primary key.  We use a String instead of an auto-
     * generated Int because the ID is assigned by the app
     * (Constants.DEFAULT_USER_ID = "local_user"), not by the DB.
     * This makes it safe to reference from ProgressEntity
     * without a foreign-key lookup.
     */
    @PrimaryKey
    val id: String,

    /** Goal category chosen during onboarding ("Fitness", "Coding" …) */
    val goal: String,

    /** Skill key used to query tasks – mirrors goal in MVP */
    val skill: String,

    /** Self-reported level: "Beginner" | "Intermediate" | "Advanced" */
    val level: String,

    /**
     * The current task day number (1-based).
     * Incremented each time a task is completed.
     */
    val currentDay: Int,

    /** Consecutive-day completion count – resets to 0 on a missed day */
    val streak: Int,

    /**
     * Unix timestamp (ms) of the most recent task completion.
     * 0 when the user has never completed a task.
     * Used to determine if today's task is already done
     * and to detect broken streaks.
     */
    val lastCompletedDate: Long
)
