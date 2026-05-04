package com.nextstep.core.domain.usecase

import com.nextstep.core.domain.model.Task
import com.nextstep.core.domain.model.User
import com.nextstep.core.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * GetTodayTaskUseCase.kt
 * ─────────────────────────────────────────────────────────
 * USE CASE: Retrieve the single task the user should do today.
 *
 * What is a use case?
 *   A use case (also called an "interactor") encapsulates one
 *   specific business action.  It:
 *     • Takes input (the current User in this case)
 *     • Applies business rules
 *     • Delegates persistence/retrieval to the repository
 *     • Returns a result
 *
 *   ViewModels call use cases, not repositories directly.
 *   This keeps business logic out of the UI layer and makes
 *   it easy to test without a real database.
 *
 * Business rules applied here:
 *   1. Fetch the task for user.skill + user.currentDay.
 *   2. If no task is found (past day 30), return null and
 *      the UI will show a "You've finished the plan!" message.
 * ─────────────────────────────────────────────────────────
 */
class GetTodayTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    /**
     * The `operator fun invoke` convention lets callers write:
     *   getTodayTask(user)
     * instead of:
     *   getTodayTask.invoke(user)
     *
     * This makes use cases feel like plain suspend functions
     * at the call site, which keeps ViewModels clean.
     */
    suspend operator fun invoke(user: User): Task? {
        return repository.getTaskByDay(user.skill, user.currentDay)
    }
}
