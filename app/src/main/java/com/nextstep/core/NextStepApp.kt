package com.nextstep.core

import android.app.Application
import com.nextstep.core.data.local.database.TaskSeeder
import com.nextstep.core.domain.repository.TaskRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * NextStepApp.kt
 * ─────────────────────────────────────────────────────────
 * The Application class is the first object created by the
 * Android runtime when the app process starts.  It lives
 * for the entire lifetime of the process.
 *
 * @HiltAndroidApp:
 *   This annotation triggers Hilt's code generation.  Hilt
 *   creates an "Application component" that serves as the
 *   root of the dependency injection graph.  Without this
 *   annotation, @Inject and @HiltViewModel won't work.
 *
 * What we do here:
 *   1. Provide an application-scoped coroutine scope so we
 *      can run suspend functions (e.g. task seeding) that
 *      must outlive any single Activity.
 *   2. Seed the task database on first launch.
 *
 * Why seed here instead of in a ViewModel?
 *   Seeding must happen BEFORE the Home screen fetches
 *   today's task.  The Application is guaranteed to be
 *   initialised before any Activity or ViewModel.
 * ─────────────────────────────────────────────────────────
 */
@HiltAndroidApp
class NextStepApp : Application() {

    /**
     * Injected by Hilt automatically because TaskRepository
     * is bound in AppModule with @Singleton scope.
     */
    @Inject
    lateinit var taskRepository: TaskRepository

    /**
     * Application-scoped coroutine scope.
     *
     * SupervisorJob: if one child coroutine fails, others
     * continue running (isolation of failures).
     *
     * Dispatchers.IO: background thread pool for disk I/O
     * operations like Room database writes.
     */
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // ── Seed the task database ────────────────────────
        // seedIfNeeded() checks whether tasks are already in
        // the database before inserting.  Safe to call on
        // every launch.
        applicationScope.launch {
            seedTasks()
        }
    }

    /**
     * Inserts the pre-defined 30-day task library into Room
     * for each goal category.  Skips categories that already
     * have rows (idempotent).
     */
    private suspend fun seedTasks() {
        val allTasks = TaskSeeder.allTasks()

        // Group tasks by category so we can check each
        // category independently
        val tasksByCategory = allTasks.groupBy { it.category }

        tasksByCategory.forEach { (category, tasks) ->
            if (!taskRepository.hasTasksForCategory(category)) {
                // Convert TaskEntity to domain Task for the repository
                taskRepository.insertTasks(
                    tasks.map { entity ->
                        com.nextstep.core.domain.model.Task(
                            id          = entity.id,
                            category    = entity.category,
                            skill       = entity.skill,
                            day         = entity.day,
                            title       = entity.title,
                            description = entity.description,
                            duration    = entity.duration
                        )
                    }
                )
            }
        }
    }
}
