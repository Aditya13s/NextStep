/*
 * settings.gradle.kts
 * ---------------------------------------------------------
 * This file is the entry point for Gradle's project
 * configuration. It defines:
 *   - The root project name
 *   - Where to find plugins and dependencies (repositories)
 *   - Which sub-projects (modules) belong to this build
 *
 * Gradle reads this file BEFORE any build.gradle file, so
 * every repository URL and version catalog must be declared
 * here for them to be available in all modules.
 * ---------------------------------------------------------
 */

// ── Plugin Management ─────────────────────────────────────
// Tells Gradle where to download the Gradle plugins that are
// applied in the build files (e.g. Android Gradle Plugin,
// Kotlin plugin, Hilt plugin).
pluginManagement {
    repositories {
        google()          // Android / Google plugins
        mavenCentral()    // Kotlin, Hilt, and other JVM plugins
        gradlePluginPortal() // Community plugins
    }
}

// ── Dependency Resolution Management ──────────────────────
// Centralises where ALL project dependencies are downloaded
// from.  This prevents individual modules from accidentally
// pulling libraries from untrusted repositories.
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()       // Android libraries (Compose, Room, Hilt …)
        mavenCentral() // Kotlin coroutines, third-party libs …
    }
    // ── Version Catalog ─────────────────────────────────
    // Loads the centralised dependency/version declarations
    // from gradle/libs.versions.toml.  Every module then
    // references a dependency as  libs.compose.ui  instead
    // of repeating "androidx.compose.ui:ui:1.x.y" everywhere.
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

// ── Root project name ─────────────────────────────────────
// Must match the folder name so IDE tooling and CI scripts
// can locate the project unambiguously.
rootProject.name = "NextStep"

// ── Sub-modules ───────────────────────────────────────────
// For now the app is a single-module project.  When we add
// nextstep.ai or nextstep.pro the additional modules will be
// included here (e.g. include(":feature:ai")).
include(":app")
