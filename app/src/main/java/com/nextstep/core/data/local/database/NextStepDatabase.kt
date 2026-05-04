package com.nextstep.core.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nextstep.core.data.local.dao.ProgressDao
import com.nextstep.core.data.local.dao.TaskDao
import com.nextstep.core.data.local.dao.UserDao
import com.nextstep.core.data.local.entity.ProgressEntity
import com.nextstep.core.data.local.entity.TaskEntity
import com.nextstep.core.data.local.entity.UserEntity
import com.nextstep.core.utils.Constants

/**
 * NextStepDatabase.kt
 * ─────────────────────────────────────────────────────────
 * The central Room database class for the app.
 *
 * @Database annotation:
 *   entities  – every @Entity class that belongs to this DB
 *   version   – schema version (increment + write a Migration
 *               when you change any Entity field name/type)
 *   exportSchema – true exports the schema to a JSON file in
 *               app/schemas/ so migrations can be audited in
 *               version control (path set in app/build.gradle)
 *
 * This class is abstract because Room's KSP processor
 * generates the concrete implementation at compile time.
 *
 * SINGLETON PATTERN:
 * Room database creation is expensive (file I/O, schema
 * validation).  The companion object ensures only ONE
 * instance is ever created per process.  Hilt's
 * @Singleton scope in AppModule guarantees that the DI
 * framework respects this too.
 * ─────────────────────────────────────────────────────────
 */
@Database(
    entities = [
        UserEntity::class,
        TaskEntity::class,
        ProgressEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class NextStepDatabase : RoomDatabase() {

    // ── DAO accessors ─────────────────────────────────────
    // Each abstract function is implemented by Room's generated
    // code.  Hilt injects the database instance and callers
    // use db.userDao() etc. through the repository.
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun progressDao(): ProgressDao

    companion object {
        /**
         * @Volatile ensures the INSTANCE write is immediately
         * visible to all threads (prevents a second thread from
         * creating a duplicate instance before the first write
         * is flushed from the CPU cache to main memory).
         */
        @Volatile
        private var INSTANCE: NextStepDatabase? = null

        /**
         * Returns the singleton database instance.
         *
         * synchronized(this) creates a critical section so that
         * even on multi-core devices only one thread can
         * evaluate the `?: run { ... }` block at a time.
         *
         * Note: In a Hilt-powered app we don't call this
         * directly – Hilt's DatabaseModule calls it once and
         * caches the result as a @Singleton.  The companion
         * object is kept as a safety net.
         */
        fun getInstance(context: Context): NextStepDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NextStepDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    // ── Migration strategy ────────────────
                    // fallbackToDestructiveMigration() drops
                    // and recreates ALL tables when the version
                    // number is bumped WITHOUT a Migration object.
                    // ONLY acceptable in early development.
                    // Replace with proper Migration objects before
                    // releasing to production users who have data.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
