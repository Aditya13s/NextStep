package com.nextstep.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * DateUtils.kt
 * ─────────────────────────────────────────────────────────
 * Pure utility functions for date/time operations.
 *
 * Why a standalone object (not an extension on Date/Calendar)?
 *   • Easier to unit-test – no Android dependencies.
 *   • Clear call site: DateUtils.isToday(ts) is self-documenting.
 *   • All date logic is in one place – no silent bugs from
 *     timezone differences scattered across the codebase.
 *
 * All timestamps stored in Room are Unix epoch milliseconds
 * (Long) so they are timezone-independent.  Display strings
 * are formatted using the device locale.
 * ─────────────────────────────────────────────────────────
 */
object DateUtils {

    // Formatter for human-readable dates shown in the UI
    private val displayFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // Formatter for shorter date strings (e.g. progress list)
    private val shortFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    /**
     * Returns the current Unix timestamp in milliseconds.
     * Using System.currentTimeMillis() (not Clock) keeps the
     * code compatible with minSdk 24 without requiring
     * desugaring for java.time.
     */
    fun nowMillis(): Long = System.currentTimeMillis()

    /**
     * Returns a Calendar set to midnight (00:00:00.000) of
     * the current day in the device's local timezone.
     *
     * This is used to compare "is timestamp on today's date"
     * without caring about the exact time.
     */
    fun todayMidnight(): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    /**
     * Returns the Unix timestamp (ms) for today at midnight.
     * Stored in Room as the "date" field of ProgressEntity so
     * one row = one calendar day.
     */
    fun todayMidnightMillis(): Long = todayMidnight().timeInMillis

    /**
     * Returns true if [timestampMillis] falls on today's
     * calendar date (ignoring time-of-day).
     *
     * Used by the streak system to decide whether the user has
     * already completed today's task.
     */
    fun isToday(timestampMillis: Long): Boolean {
        val today = todayMidnight()
        val tomorrow = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }
        return timestampMillis >= today.timeInMillis && timestampMillis < tomorrow.timeInMillis
    }

    /**
     * Returns true if [timestampMillis] falls on yesterday's
     * calendar date.
     *
     * Used by the streak system: if the last completed date was
     * yesterday, the streak is still alive; if it was earlier
     * than yesterday, the streak is broken (reset to 0).
     */
    fun isYesterday(timestampMillis: Long): Boolean {
        val yesterday = todayMidnight().apply { add(Calendar.DAY_OF_YEAR, -1) }
        val today = todayMidnight()
        return timestampMillis >= yesterday.timeInMillis && timestampMillis < today.timeInMillis
    }

    /**
     * Formats a Unix timestamp as "dd MMM yyyy" (e.g. "04 May 2025").
     * Used in the progress history list.
     */
    fun formatDate(timestampMillis: Long): String =
        displayFormat.format(Date(timestampMillis))

    /**
     * Formats a Unix timestamp as "dd MMM" (e.g. "04 May").
     * Used in compact progress cards.
     */
    fun formatShortDate(timestampMillis: Long): String =
        shortFormat.format(Date(timestampMillis))

    /**
     * Returns the number of whole days between two timestamps.
     * Used when calculating streak gaps.
     */
    fun daysBetween(fromMillis: Long, toMillis: Long): Long {
        val diff = toMillis - fromMillis
        return TimeUnit.MILLISECONDS.toDays(diff)
    }
}
