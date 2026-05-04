package com.nextstep.core.utils

/**
 * Constants.kt
 * ─────────────────────────────────────────────────────────
 * Central location for every "magic value" used across the
 * app.  Keeping constants here means:
 *   • Easy to find and change values in one place.
 *   • No risk of typos when the same string is used in
 *     multiple files.
 *   • Compiler enforces correct types (Int vs String).
 * ─────────────────────────────────────────────────────────
 */
object Constants {

    // ── DataStore ─────────────────────────────────────────
    // DataStore is a key-value store similar to SharedPreferences
    // but coroutine-based and safer.  We use it to remember
    // whether the user has completed onboarding so we can skip
    // it on subsequent launches.
    const val DATASTORE_NAME = "nextstep_prefs"
    const val KEY_ONBOARDING_DONE = "onboarding_done"

    // ── Room Database ─────────────────────────────────────
    // The name of the SQLite database file stored on device.
    const val DATABASE_NAME = "nextstep_database"

    // ── Default user ID ───────────────────────────────────
    // The MVP supports a single local user.  We use a fixed
    // ID so every DAO query can reference it without requiring
    // an auth system.
    const val DEFAULT_USER_ID = "local_user"

    // ── Streak levels ─────────────────────────────────────
    // These thresholds determine the gamification badge shown
    // on the home screen.
    const val STREAK_BRONZE_MIN = 1
    const val STREAK_SILVER_MIN = 7
    const val STREAK_GOLD_MIN   = 30

    // ── Task constraints ──────────────────────────────────
    // Maximum task duration in minutes (as per the product spec)
    const val MAX_TASK_DURATION_MINUTES = 30

    // ── Goals ─────────────────────────────────────────────
    // The set of goals the user can choose during onboarding.
    // Each goal maps to a category of pre-seeded tasks in the
    // database.
    val AVAILABLE_GOALS = listOf(
        "Fitness",
        "Study",
        "Productivity",
        "Mental Health",
        "Coding"
    )

    // ── Skill levels ──────────────────────────────────────
    val SKILL_LEVELS = listOf("Beginner", "Intermediate", "Advanced")

    // ── Navigation routes ─────────────────────────────────
    // String routes used by the NavController.  Using an
    // object prevents accidental typos in navigate() calls.
    object Routes {
        const val ONBOARDING = "onboarding"
        const val HOME        = "home"
        const val PROGRESS    = "progress"
        const val PROFILE     = "profile"
    }
}
