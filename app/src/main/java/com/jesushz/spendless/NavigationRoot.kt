package com.jesushz.spendless

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.auth.presentation.login.LoginScreenRoot
import com.jesushz.spendless.auth.presentation.pin.PinScreenRoot
import com.jesushz.spendless.auth.presentation.register.RegisterScreenRoot
import com.jesushz.spendless.core.util.Routes
import com.jesushz.spendless.dashboard.presentation.all_transactions.AllTransactionsScreenRoot
import com.jesushz.spendless.dashboard.presentation.dashboard.DashboardScreenRoot
import com.jesushz.spendless.dashboard.presentation.preferences.PreferencesScreenRoot
import com.jesushz.spendless.settings.presentation.settings.SettingsScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Routes = Routes.AuthGraph
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController)
        dashboardGraph(navController)
        settingsGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation<Routes.AuthGraph>(
        startDestination = Routes.RegisterScreen
    ) {
        composable<Routes.RegisterScreen> {
            RegisterScreenRoot(
                onNavigateToPin = { username ->
                    navController.navigate(Routes.PinScreen(PinFlow.REGISTER, username))
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.LoginScreen)
                }
            )
        }

        composable<Routes.PinScreen> {
            PinScreenRoot(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToPreferences = {
                    navController.navigate(Routes.PreferencesScreen)
                }
            )
        }

        composable<Routes.LoginScreen> {
            LoginScreenRoot(
                onNavigateToRegister = {
                    navController.navigate(Routes.LoginScreen)
                },
                onNavigateToDashboard = {
                    navController.navigate(Routes.DashboardGraph)
                }
            )
        }
    }
}

private fun NavGraphBuilder.dashboardGraph(
    navController: NavHostController
) {
    navigation<Routes.DashboardGraph>(
        startDestination = Routes.DashboardScreen
    ) {
        composable<Routes.DashboardScreen> {
            DashboardScreenRoot(
                onNavigateToShowAllTransactions = {
                    navController.navigate(Routes.AllTransactionsScreen)
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SettingsGraph)
                }
            )
        }

        composable<Routes.AllTransactionsScreen> {
            AllTransactionsScreenRoot(
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}

private fun NavGraphBuilder.settingsGraph(
    navController: NavHostController
) {
    navigation<Routes.SettingsGraph>(
        startDestination = Routes.SettingsScreen
    ) {
        composable<Routes.SettingsScreen> {
            SettingsScreenRoot(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToPreferences = {
                    navController.navigate(Routes.PreferencesScreen)
                },
                onNavigateToSecurity = {}
            )
        }

        composable<Routes.PreferencesScreen> {
            PreferencesScreenRoot(
                onNavigateToDashboard = {
                    navController.navigate(Routes.DashboardScreen)
                }
            )
        }
    }
}
