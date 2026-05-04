package com.nextstep.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.nextstep.core.presentation.navigation.NavGraph
import com.nextstep.core.presentation.theme.NextStepTheme
import com.nextstep.core.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * MainActivity.kt
 * ─────────────────────────────────────────────────────────
 * The single Activity in this Single-Activity Architecture.
 *
 * Responsibilities:
 *   1. Install the splash screen (Android 12+ API).
 *   2. Read the "onboarding done" flag from DataStore to
 *      determine the start destination for the NavGraph.
 *   3. Set the Compose content with NextStepTheme applied.
 *
 * @AndroidEntryPoint:
 *   Marks this Activity for Hilt injection.  Without this,
 *   @Inject fields and hiltViewModel() won't work in this
 *   Activity or in any composable called from it.
 *
 * Why ComponentActivity instead of AppCompatActivity?
 *   Jetpack Compose only needs ComponentActivity.  AppCompat
 *   is for View-based UIs and adds unnecessary overhead.
 * ─────────────────────────────────────────────────────────
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * DataStore<Preferences> injected by Hilt.
     * Used to read whether onboarding has been completed.
     */
    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen BEFORE super.onCreate() so the
        // splash is shown while the app initialises.
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // ── Splash screen keep-on condition ───────────────
        // We could hold the splash until DataStore is read,
        // but for simplicity we let it dismiss immediately
        // and show a loading state in the NavGraph if needed.

        setContent {
            // ── Read onboarding flag ──────────────────────
            // collectAsState() subscribes to the Flow on the
            // composition coroutine scope.  The initial value
            // is null (unknown) – we start at ONBOARDING and
            // if the flag is already true, NavGraph navigates
            // to HOME immediately.
            val onboardingDoneKey = booleanPreferencesKey(Constants.KEY_ONBOARDING_DONE)
            val startDestination by dataStore.data
                .map { prefs ->
                    if (prefs[onboardingDoneKey] == true)
                        Constants.Routes.HOME
                    else
                        Constants.Routes.ONBOARDING
                }
                .collectAsState(initial = null)

            NextStepTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Wait until we know the start destination
                    // before rendering the NavGraph.  This avoids
                    // a brief flash of the onboarding screen on
                    // repeat launches.
                    val dest = startDestination
                    if (dest != null) {
                        NavGraph(startDestination = dest)
                    }
                }
            }
        }
    }
}
