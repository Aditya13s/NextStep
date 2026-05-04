package com.nextstep.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nextstep.core.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * UserDao.kt
 * ─────────────────────────────────────────────────────────
 * Data Access Object for the "user" table.
 *
 * @Dao tells Room this is a DAO interface.  Room's KSP
 * processor generates the concrete implementation class at
 * compile time.  We never instantiate this interface manually
 * – Room creates it through NextStepDatabase.userDao().
 *
 * Design notes:
 *   • @Query methods are checked against the actual schema at
 *     compile time – typos cause a build error, not a runtime
 *     crash.
 *   • Flow<T?> return types create a "live query" that emits a
 *     new value every time the underlying table row changes.
 *     This powers the reactive UI without any manual refresh.
 *   • suspend functions run on the coroutine dispatcher
 *     provided by Room (IO by default when using room-ktx).
 * ─────────────────────────────────────────────────────────
 */
@Dao
interface UserDao {

    /**
     * Inserts or fully replaces the user row.
     *
     * OnConflictStrategy.REPLACE: if a row with the same
     * primary key already exists, the old row is deleted and
     * the new one is inserted.  This is the simplest way to
     * handle both INSERT (first onboarding) and UPDATE
     * (changing goal, streak update) with a single DAO method.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: UserEntity)

    /**
     * Returns a live Flow of the user row.
     * Emits null when no row with [userId] exists (before
     * onboarding completes).
     * The UI uses this Flow to automatically recompose when
     * the streak or currentDay changes.
     */
    @Query("SELECT * FROM user WHERE id = :userId")
    fun observeUser(userId: String): Flow<UserEntity?>

    /**
     * One-shot snapshot read used in use cases that need the
     * current state but don't need a live subscription.
     */
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUser(userId: String): UserEntity?
}
