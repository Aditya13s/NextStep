/*
 * app/build.gradle.kts  (module-level)
 * ---------------------------------------------------------
 * This file configures the :app module.  It declares:
 *   - Which Gradle plugins are applied to this module
 *   - Android build configuration (SDK versions, build types)
 *   - All runtime and compile-time dependencies
 *   - KSP (code generation) processors for Room & Hilt
 * ---------------------------------------------------------
 */

// ── Apply plugins ─────────────────────────────────────────
plugins {
    // Turns this Kotlin module into a deployable Android app
    alias(libs.plugins.android.application)

    // Enables Kotlin compilation for Android targets
    alias(libs.plugins.kotlin.android)

    // KSP – generates boilerplate code for Room DAOs and
    // Hilt injection components at compile time
    alias(libs.plugins.kotlin.ksp)

    // Hilt – triggers Dagger component generation via KSP
    alias(libs.plugins.hilt)
}

// ── Android configuration block ───────────────────────────
android {
    // The unique identifier for this app on the Play Store
    // and on the device.  Must match the root package of the
    // Kotlin source files.
    namespace = "com.nextstep.core"

    // The version of the Android SDK used to compile the app.
    // Always use the latest stable SDK to get new APIs.
    compileSdk = 34

    defaultConfig {
        // Must match the namespace above
        applicationId = "com.nextstep.core"

        // Minimum Android version the app supports.
        // SDK 24 = Android 7.0 (Nougat) – covers ~97 % of
        // active devices as of 2024.
        minSdk = 24

        // The version of Android the app is tested against
        // and optimised for.
        targetSdk = 34

        // Internal build number incremented on every release
        versionCode = 1

        // Human-readable version string shown on the Play Store
        versionName = "1.0.0"

        // Use the JUnit4 test runner for instrumented (on-device)
        // tests run via  ./gradlew connectedAndroidTest
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ── Room schema export ──────────────────────────
        // Exports the Room database schema to a JSON file on
        // every build.  This file should be committed to version
        // control so schema migrations can be validated in CI.
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        // ── Release build ──────────────────────────────
        // Used for Play Store uploads.  Minification (R8)
        // shrinks and obfuscates the code to reduce APK size.
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        // ── Debug build ────────────────────────────────
        // Used during development.  Minification is disabled
        // for faster builds and easier debugging.
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    // ── Java / Kotlin compatibility ───────────────────────
    // JVM 17 is the minimum required by AGP 8.x and Kotlin
    // 1.9.x.  Both the Kotlin compiler and the Java source
    // compatibility must be set to the same version.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    // ── Jetpack Compose ───────────────────────────────────
    buildFeatures {
        // Enables the Compose compiler plugin.  Without this
        // flag the @Composable annotation is not recognised.
        compose = true
    }
    composeOptions {
        // The Compose compiler extension version must be
        // compatible with the Kotlin compiler version declared
        // in the version catalog.
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

// ── Dependencies ──────────────────────────────────────────
dependencies {

    // ── AndroidX core ─────────────────────────────────────
    // Provides Kotlin extension functions for common Android
    // APIs (e.g. context.getSystemService<...>()).
    implementation(libs.androidx.core.ktx)

    // Bridges Compose with the Activity lifecycle so the
    // Compose content can be set inside ComponentActivity.
    implementation(libs.androidx.activity.compose)

    // Android 12+ Splash Screen API – shows the app icon
    // while the first frame is being drawn.
    implementation(libs.androidx.splashscreen)

    // DataStore – asynchronous, coroutine-friendly key-value
    // storage used to persist the "first launch" flag.
    implementation(libs.androidx.datastore.preferences)

    // ── Lifecycle ─────────────────────────────────────────
    // Bundle pulls in viewmodel-ktx, runtime-compose, and
    // runtime-ktx together (see libs.versions.toml [bundles]).
    implementation(libs.bundles.lifecycle)

    // ── Jetpack Compose ───────────────────────────────────
    // The BOM (Bill of Materials) ensures all Compose
    // libraries use compatible versions automatically.
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)

    // The bundle pulls in UI, Graphics, Material3, Icons,
    // Animation, and the interactive tooling-preview.
    implementation(libs.bundles.compose)

    // Needed only in debug builds so the Compose layout
    // inspector works in Android Studio.
    debugImplementation(libs.androidx.compose.ui.tooling)

    // ── Navigation ────────────────────────────────────────
    // Type-safe navigation between Compose screens using a
    // NavController.
    implementation(libs.androidx.navigation.compose)

    // ── Room ──────────────────────────────────────────────
    // Room is an abstraction layer over SQLite.
    // room-runtime  – core runtime (DB + DAO boilerplate)
    // room-ktx      – suspending / Flow extension functions
    // room-compiler – KSP annotation processor that generates
    //                 the concrete DAO implementation classes
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    // ── Hilt ──────────────────────────────────────────────
    // hilt-android            – runtime DI framework
    // hilt-compiler           – KSP processor that generates
    //                           Dagger components & factories
    // hilt-navigation-compose – hiltViewModel() helper that
    //                           scopes ViewModels to the nav
    //                           back-stack entry
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // ── Coroutines ────────────────────────────────────────
    // Adds Android-specific dispatchers (Dispatchers.Main)
    // and coroutine scope integrations for ViewModel.
    implementation(libs.kotlinx.coroutines.android)

    // ── Unit tests ────────────────────────────────────────
    testImplementation(libs.junit)

    // ── Instrumented (on-device) tests ────────────────────
    androidTestImplementation(libs.androidx.junit.ext)
    androidTestImplementation(libs.androidx.espresso)
    androidTestImplementation(composeBom)
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
