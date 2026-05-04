package com.nextstep.core.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nextstep.core.presentation.home.HomeScreen
import com.nextstep.core.presentation.onboarding.OnboardingScreen
import com.nextstep.core.presentation.profile.ProfileScreen
import com.nextstep.core.presentation.progress.ProgressScreen
import com.nextstep.core.utils.Constants.Routes

/**
 * NavGraph.kt
 * ─────────────────────────────────────────────────────────
 * Defines the full navigation graph for the app.
 *
 * Architecture:
 *   • One NavController manages all navigation.
 *   • Onboarding is a separate destination (no bottom bar).
 *   • Home, Progress, and Profile share a BottomNavigationBar.
 *
 * Screen transitions:
 *   • Slide left/right between bottom-nav tabs feels natural.
 *   • Onboarding → Home fades for a clean "launch" feel.
 *
 * @param startDestination  Routes.ONBOARDING on first launch,
 *                          Routes.HOME on subsequent launches.
 *                          Determined in MainActivity by checking
 *                          DataStore for the onboarding flag.
 * ─────────────────────────────────────────────────────────
 */
@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()

    // Bottom navigation items definition
    val bottomNavItems = listOf(
        BottomNavItem("Home",     Routes.HOME,     Icons.Default.Home),
        BottomNavItem("Progress", Routes.PROGRESS, Icons.Default.ShowChart),
        BottomNavItem("Profile",  Routes.PROFILE,  Icons.Default.Person)
    )

    // Get the current back-stack entry to highlight the
    // active bottom nav item.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show the bottom nav bar on the three main screens
    val showBottomBar = currentDestination?.route in listOf(
        Routes.HOME, Routes.PROGRESS, Routes.PROFILE
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            // Highlight this item when the current
                            // destination is this item's route or
                            // any destination in its sub-graph.
                            selected = currentDestination?.hierarchy
                                ?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Pop up to the start destination to
                                    // avoid building up a large back stack.
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination
                                    launchSingleTop = true
                                    // Restore state when re-selecting a previously
                                    // selected tab
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = startDestination,
            modifier         = Modifier.padding(innerPadding),
            // Default transition: slide from the right edge
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(300)
                )
            }
        ) {
            // ── Onboarding ─────────────────────────────────
            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onComplete = {
                        // Replace the onboarding destination so the
                        // user can't go back to it with the Back button.
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }

            // ── Home ───────────────────────────────────────
            composable(Routes.HOME) {
                HomeScreen(
                    onNavigateToProfile = {
                        navController.navigate(Routes.PROFILE) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            // ── Progress ───────────────────────────────────
            composable(Routes.PROGRESS) {
                ProgressScreen()
            }

            // ── Profile ────────────────────────────────────
            composable(Routes.PROFILE) {
                ProfileScreen()
            }
        }
    }
}

/**
 * BottomNavItem  –  data class for a bottom nav tab entry
 */
data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
