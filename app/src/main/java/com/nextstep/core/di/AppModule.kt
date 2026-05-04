package com.nextstep.core.di

import com.nextstep.core.data.repository.TaskRepositoryImpl
import com.nextstep.core.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule.kt
 * ─────────────────────────────────────────────────────────
 * Hilt module that binds domain interfaces to their concrete
 * data-layer implementations.
 *
 * Why @Binds instead of @Provides?
 *   @Provides runs a function body to create an object.
 *   @Binds just tells Hilt "when someone asks for interface X,
 *   give them concrete class Y".  It generates less code and
 *   has zero runtime overhead.
 *
 *   @Provides would look like:
 *     fun provideRepo(impl: TaskRepositoryImpl): TaskRepository = impl
 *   @Binds replaces that with a single abstract function
 *   declaration – Hilt handles the wiring automatically.
 *
 * The module must be abstract because @Binds functions must
 * be abstract (they have no body – Hilt provides the body).
 * ─────────────────────────────────────────────────────────
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Tells Hilt: whenever a constructor or function requests
     * a TaskRepository, inject a TaskRepositoryImpl.
     *
     * TaskRepositoryImpl is annotated with @Singleton so Hilt
     * creates one instance and reuses it across the app.
     */
    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}
