package com.nextstep.core

import com.nextstep.core.domain.model.User
import com.nextstep.core.utils.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

/**
 * UserStreakLevelTest.kt
 * ─────────────────────────────────────────────────────────
 * Unit tests for the User domain model's computed properties.
 *
 * These tests validate:
 *   1. streakLevel badge assignment thresholds
 *   2. hasCompletedToday() logic
 *
 * Pure JVM tests – no Android framework needed.
 * ─────────────────────────────────────────────────────────
 */
class UserStreakLevelTest {

    private fun makeUser(streak: Int, lastCompleted: Long = 0L) = User(
        id = "test",
        goal = "Fitness",
        skill = "Fitness",
        level = "Beginner",
        currentDay = 1,
        streak = streak,
        lastCompletedDate = lastCompleted
    )

    @Test
    fun `streakLevel is dash when streak is zero`() {
        assertEquals("–", makeUser(0).streakLevel)
    }

    @Test
    fun `streakLevel is Bronze for streak 1 to 6`() {
        (1..6).forEach { streak ->
            val level = makeUser(streak).streakLevel
            assertEquals("Expected Bronze for streak $streak", true, level.contains("Bronze"))
        }
    }

    @Test
    fun `streakLevel is Silver for streak 7 to 29`() {
        (7..29).forEach { streak ->
            val level = makeUser(streak).streakLevel
            assertEquals("Expected Silver for streak $streak", true, level.contains("Silver"))
        }
    }

    @Test
    fun `streakLevel is Gold for streak 30 and above`() {
        listOf(30, 50, 100).forEach { streak ->
            val level = makeUser(streak).streakLevel
            assertEquals("Expected Gold for streak $streak", true, level.contains("Gold"))
        }
    }

    @Test
    fun `hasCompletedToday returns false when lastCompletedDate is zero`() {
        val user = makeUser(0, lastCompleted = 0L)
        assertEquals(false, user.hasCompletedToday())
    }

    @Test
    fun `hasCompletedToday returns true when lastCompletedDate is today`() {
        val now = DateUtils.nowMillis()
        val user = makeUser(1, lastCompleted = now)
        assertEquals(true, user.hasCompletedToday())
    }

    @Test
    fun `hasCompletedToday returns false when lastCompletedDate is yesterday`() {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.timeInMillis
        val user = makeUser(1, lastCompleted = yesterday)
        assertEquals(false, user.hasCompletedToday())
    }
}
