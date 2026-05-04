package com.nextstep.core

import com.nextstep.core.utils.DateUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

/**
 * DateUtilsTest.kt
 * ─────────────────────────────────────────────────────────
 * Unit tests for DateUtils functions.
 *
 * These are pure JVM tests (no Android dependencies) because
 * DateUtils uses only java.util.* classes.  They run with:
 *   ./gradlew :app:test
 *
 * Why test DateUtils?
 *   The streak system DEPENDS on isToday() and isYesterday()
 *   being correct.  A bug here would silently break streaks
 *   for ALL users.
 * ─────────────────────────────────────────────────────────
 */
class DateUtilsTest {

    @Test
    fun `isToday returns true for current timestamp`() {
        val now = DateUtils.nowMillis()
        assertTrue("Current time should be 'today'", DateUtils.isToday(now))
    }

    @Test
    fun `isToday returns false for yesterday`() {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.timeInMillis
        assertFalse("Yesterday should NOT be 'today'", DateUtils.isToday(yesterday))
    }

    @Test
    fun `isToday returns false for tomorrow`() {
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
        }.timeInMillis
        assertFalse("Tomorrow should NOT be 'today'", DateUtils.isToday(tomorrow))
    }

    @Test
    fun `isYesterday returns true for yesterday`() {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.timeInMillis
        assertTrue("Yesterday should be 'yesterday'", DateUtils.isYesterday(yesterday))
    }

    @Test
    fun `isYesterday returns false for today`() {
        val now = DateUtils.nowMillis()
        assertFalse("Today should NOT be 'yesterday'", DateUtils.isYesterday(now))
    }

    @Test
    fun `isYesterday returns false for two days ago`() {
        val twoDaysAgo = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -2)
        }.timeInMillis
        assertFalse("2 days ago should NOT be 'yesterday'", DateUtils.isYesterday(twoDaysAgo))
    }

    @Test
    fun `todayMidnightMillis is before nowMillis`() {
        val midnight = DateUtils.todayMidnightMillis()
        val now = DateUtils.nowMillis()
        assertTrue("Midnight should be before (or equal to) now", midnight <= now)
    }

    @Test
    fun `daysBetween returns correct count`() {
        val now = DateUtils.nowMillis()
        val sevenDaysAgo = now - (7 * 24 * 60 * 60 * 1000L)
        val days = DateUtils.daysBetween(sevenDaysAgo, now)
        assertTrue("Should be 7 days", days == 7L)
    }
}
