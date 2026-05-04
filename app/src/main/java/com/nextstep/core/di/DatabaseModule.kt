package com.nextstep.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nextstep.core.data.local.database.NextStepDatabase
import com.nextstep.core.data.local.dao.ProgressDao
import com.nextstep.core.data.local.dao.TaskDao
import com.nextstep.core.data.local.dao.UserDao
import com.nextstep.core.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Extension property that creates / retrieves the DataStore
// instance tied to this Context.  Declared at top-level to
// comply with the DataStore API requirement (one instance
// per process, not per call site).
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.DATASTORE_NAME
)

/**
 * DatabaseModule.kt
 * ─────────────────────────────────────────────────────────
 * Hilt module that provides Room database + DAO instances.
 *
 * @Module        – marks this object as a Hilt DI module
 * @InstallIn     – scopes this module to the Application
 *                  component (lives as long as the app process)
 *
 * Why separate DatabaseModule from AppModule?
 *   Splitting modules by concern keeps each file small and
 *   focused.  If you later add a network module (Retrofit),
 *   it goes in its own NetworkModule – no giant AppModule.
 * ─────────────────────────────────────────────────────────
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the singleton Room database instance.
     *
     * @Singleton ensures Hilt calls this function ONCE and
     * caches the result.  Every injection point that requests
     * NextStepDatabase gets the same object.
     *
     * @ApplicationContext is a Hilt qualifier that supplies the
     * Application Context (not an Activity context), which is
     * safe to hold in a singleton.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NextStepDatabase =
        NextStepDatabase.getInstance(context)

    /**
     * Provides UserDao by calling db.userDao().
     * The database instance is injected by Hilt (from the
     * function above).
     *
     * @Singleton – Room DAO instances are lightweight wrappers
     * over the database; there is no cost to sharing them.
     */
    @Provides
    @Singleton
    fun provideUserDao(db: NextStepDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideTaskDao(db: NextStepDatabase): TaskDao = db.taskDao()

    @Provides
    @Singleton
    fun provideProgressDao(db: NextStepDatabase): ProgressDao = db.progressDao()

    /**
     * Provides the DataStore<Preferences> instance.
     *
     * DataStore is used to persist the "onboarding completed"
     * flag so we can skip onboarding on subsequent launches
     * without querying Room.
     */
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}
