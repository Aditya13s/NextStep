package com.nextstep.core.data.repository

import com.nextstep.core.data.local.dao.ProgressDao
import com.nextstep.core.data.local.dao.TaskDao
import com.nextstep.core.data.local.dao.UserDao
import com.nextstep.core.data.local.entity.ProgressEntity
import com.nextstep.core.data.local.entity.TaskEntity
import com.nextstep.core.data.local.entity.UserEntity
import com.nextstep.core.domain.model.Task
import com.nextstep.core.domain.model.User
import com.nextstep.core.domain.repository.ProgressEntry
import com.nextstep.core.domain.repository.TaskRepository
import com.nextstep.core.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TaskRepositoryImpl.kt  (Data layer)
 * ─────────────────────────────────────────────────────────
 * The CONCRETE implementation of the domain's TaskRepository
 * interface.
 *
 * This class is the bridge between:
 *   Domain layer (pure Kotlin models + business rules)
 *         ↕
 *   Data layer (Room entities + DAOs)
 *
 * Responsibilities:
 *   1. Delegate storage reads/writes to the appropriate DAO.
 *   2. Map between domain models (User, Task) and Room
 *      entities (UserEntity, TaskEntity) – domain code never
 *      imports Room annotations.
 *   3. Transform Flow<Entity> → Flow<DomainModel> using .map{}
 *      so the UI receives clean domain objects.
 *
 * @Singleton means Hilt creates ONE instance and reuses it
 * everywhere.  Since the Room DAOs hold no state themselves,
 * a singleton repository is safe and efficient.
 * ─────────────────────────────────────────────────────────
 */
@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val taskDao: TaskDao,
    private val progressDao: ProgressDao
) : TaskRepository {

    // ── User operations ───────────────────────────────────

    /**
     * Returns a live Flow of the user, mapping the nullable
     * UserEntity to a nullable domain User.
     *
     * The .map {} operator transforms each emission from the
     * Flow without breaking the reactive chain – when the
     * Room row changes, the Flow automatically emits a new
     * mapped User to all collectors (ViewModels).
     */
    override fun observeUser(userId: String): Flow<User?> =
        userDao.observeUser(userId).map { entity -> entity?.toDomain() }

    override suspend fun getUser(userId: String): User? =
        userDao.getUser(userId)?.toDomain()

    override suspend fun saveUser(user: User) =
        userDao.insertOrUpdate(user.toEntity())

    // ── Task operations ───────────────────────────────────

    override suspend fun getTaskByDay(skill: String, day: Int): Task? =
        taskDao.getTaskByDay(skill, day)?.toDomain()

    override suspend fun hasTasksForCategory(category: String): Boolean =
        taskDao.countTasksForCategory(category) > 0

    override suspend fun insertTasks(tasks: List<Task>) =
        taskDao.insertTasks(tasks.map { it.toEntity() })

    // ── Progress / streak operations ──────────────────────

    /**
     * Records a task completion in the progress table.
     *
     * Steps:
     * 1. Look up the task for (skill=user.skill, day=currentDay)
     *    to get its ID for the foreign key in ProgressEntity.
     * 2. Insert a ProgressEntity row.
     *
     * Note: The caller (CompleteTaskUseCase) is responsible for
     * updating the User's streak and currentDay separately via
     * saveUser().  This keeps each operation atomic and avoids
     * a god-function that does too many things.
     */
    override suspend fun markTaskCompleted(userId: String, day: Int) {
        // Look up the current user to get their skill category
        val user = userDao.getUser(userId) ?: return
        val task = taskDao.getTaskByDay(user.skill, day) ?: return

        progressDao.insert(
            ProgressEntity(
                userId = userId,
                taskId = task.id,
                completed = true,
                date = DateUtils.todayMidnightMillis()
            )
        )
    }

    /**
     * Returns a live Flow of progress entries with task titles.
     *
     * The ProgressDao's JOIN query returns ProgressWithTitle rows;
     * we map them to the domain's ProgressEntry to keep the
     * domain free of DAO-specific types.
     */
    override fun observeProgress(userId: String): Flow<List<ProgressEntry>> =
        progressDao.observeProgressWithTitles(userId).map { list ->
            list.map { row ->
                ProgressEntry(
                    taskTitle = row.taskTitle,
                    completedDate = row.date,
                    day = row.taskDay
                )
            }
        }

    override suspend fun getTotalCompleted(userId: String): Int =
        progressDao.countCompleted(userId)

    // ── Mapping extension functions ───────────────────────
    // These private extension functions convert between Room
    // entities and domain models.  They live here (not in the
    // entity classes) to keep the data and domain layers truly
    // separate – the entity classes don't know about domain.

    private fun UserEntity.toDomain() = User(
        id = id,
        goal = goal,
        skill = skill,
        level = level,
        currentDay = currentDay,
        streak = streak,
        lastCompletedDate = lastCompletedDate
    )

    private fun User.toEntity() = UserEntity(
        id = id,
        goal = goal,
        skill = skill,
        level = level,
        currentDay = currentDay,
        streak = streak,
        lastCompletedDate = lastCompletedDate
    )

    private fun TaskEntity.toDomain() = Task(
        id = id,
        category = category,
        skill = skill,
        day = day,
        title = title,
        description = description,
        duration = duration
    )

    private fun Task.toEntity() = TaskEntity(
        id = id,
        category = category,
        skill = skill,
        day = day,
        title = title,
        description = description,
        duration = duration
    )
}
