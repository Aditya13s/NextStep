package com.nextstep.core.domain.model

/**
 * Task.kt  (Domain model)
 * ─────────────────────────────────────────────────────────
 * Domain representation of a single actionable daily task.
 *
 * Pre-defined tasks are seeded into the Room database on the
 * first app launch (see TaskSeeder).  During each session the
 * task engine fetches one task by (skill, day) and presents
 * it as "today's step".
 *
 * This class is intentionally NOT annotated with @Entity –
 * that's TaskEntity in the data layer.  Keeping domain and
 * data models separate means the UI never needs to import
 * Room annotations.
 * ─────────────────────────────────────────────────────────
 */
data class Task(

    /** Auto-generated primary key in the database. */
    val id: Int,

    /**
     * Goal category this task belongs to.
     * Matches the values in Constants.AVAILABLE_GOALS.
     * e.g. "Fitness", "Coding", "Study"
     */
    val category: String,

    /**
     * Specific skill within the category.
     * In MVP this mirrors category, but allows future
     * expansion (e.g. category="Fitness", skill="Yoga").
     */
    val skill: String,

    /**
     * Which day in the 30-day plan this task corresponds to
     * (1-indexed).  The task engine fetches by (skill, day).
     */
    val day: Int,

    /**
     * Short, motivating task headline shown on the Home screen.
     * Must be clear and action-oriented.
     * Example: "Do 20 push-ups right now"
     */
    val title: String,

    /**
     * One-to-three sentence explanation of WHY this task
     * matters and HOW to do it correctly.
     * Shown when the user taps the task card for more detail.
     */
    val description: String,

    /**
     * Estimated time to complete in minutes.
     * Per the product spec, every task must be ≤ 30 minutes.
     */
    val duration: Int
)
