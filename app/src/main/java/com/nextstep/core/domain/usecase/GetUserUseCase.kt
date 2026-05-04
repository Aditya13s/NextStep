package com.nextstep.core.domain.usecase

import com.nextstep.core.domain.model.User
import com.nextstep.core.domain.repository.TaskRepository
import com.nextstep.core.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * GetUserUseCase.kt
 * ─────────────────────────────────────────────────────────
 * USE CASE: Observe the current user as a live Flow.
 *
 * The home screen and profile screen subscribe to this Flow
 * so they automatically recompose when the User changes
 * (e.g. after CompleteTaskUseCase runs and the streak/day
 * numbers update).
 *
 * Why wrap observeUser() in a use case instead of calling
 * the repository directly from the ViewModel?
 *   • Consistent pattern – every action goes through a
 *     use case, making the architecture uniform.
 *   • Easier to add cross-cutting logic later (e.g. cache
 *     expiry, logging) without touching the ViewModel.
 * ─────────────────────────────────────────────────────────
 */
class GetUserUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<User?> =
        repository.observeUser(Constants.DEFAULT_USER_ID)
}
