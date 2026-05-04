/*
 * build.gradle.kts  (project-level / root)
 * ---------------------------------------------------------
 * This file applies Gradle plugins to the ROOT project.
 * It does NOT declare any dependencies directly – those
 * belong in app/build.gradle.kts.
 *
 * The  apply false  flag means "make this plugin available
 * to sub-modules but DON'T apply it here at the root".
 * Sub-modules (e.g. :app) opt-in with  plugins { ... }
 * in their own build file.
 * ---------------------------------------------------------
 */

// ── Plugin declarations ───────────────────────────────────
plugins {
    // Android Gradle Plugin – turns Kotlin code into an APK/AAB
    alias(libs.plugins.android.application) apply false

    // Kotlin compiler plugin for Android targets
    alias(libs.plugins.kotlin.android) apply false

    // KSP (Kotlin Symbol Processing) – compile-time code
    // generation used by both Room (generates DAO impls) and
    // Hilt (generates DI component/factory code)
    alias(libs.plugins.kotlin.ksp) apply false

    // Hilt – Dagger-based dependency injection framework
    alias(libs.plugins.hilt) apply false
}
