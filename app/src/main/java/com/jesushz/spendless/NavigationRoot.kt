package com.jesushz.spendless

import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.auth.presentation.login.LoginAction
import com.jesushz.spendless.auth.presentation.login.LoginScreenRoot
import com.jesushz.spendless.auth.presentation.login.LoginViewModel
import com.jesushz.spendless.auth.presentation.pin.PinAction
import com.jesushz.spendless.auth.presentation.pin.PinScreenRoot
import com.jesushz.spendless.auth.presentation.pin.PinViewModel
import com.jesushz.spendless.auth.presentation.register.RegisterScreenRoot
import com.jesushz.spendless.core.data.security.BiometricPromptManager.BiometricResult
import com.jesushz.spendless.core.domain.preferences.PrefsFlow
import com.jesushz.spendless.core.presentation.ui.ObserveAsEvents
import com.jesushz.spendless.core.presentation.ui.UiText
import com.jesushz.spendless.core.util.Routes
import com.jesushz.spendless.dashboard.presentation.all_transactions.AllTransactionsScreenRoot
import com.jesushz.spendless.dashboard.presentation.dashboard.DashboardScreenRoot
import com.jesushz.spendless.settings.presentation.preferences.PreferencesScreenRoot
import com.jesushz.spendless.settings.presentation.security.SecurityScreenRoot
import com.jesushz.spendless.settings.presentation.settings.SettingsScreenRoot
import kotlinx.coroutines.flow.emptyFlow
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

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
        startDestination = Routes.RegisterScreen,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) }
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
            val activity = LocalActivity.current as? MainActivity

            val viewModel: PinViewModel = koinViewModel()

            val enrollLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = {
                    Timber.i("Activity result: $it")
                }
            )

            ObserveAsEvents(
                flow = activity?.promptManager?.promptResults ?: emptyFlow<BiometricResult>()
            ) { result ->
                when(result) {
                    is BiometricResult.AuthenticationError -> {
                        viewModel.onAction(
                            PinAction.OnBiometricsError(
                                UiText.DynamicString(result.error)
                            )
                        )
                    }
                    BiometricResult.AuthenticationFailed -> {
                        Timber.e("Authentication failed")
                    }
                    BiometricResult.AuthenticationNotSet -> {
                        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                            putExtra(
                                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                            )
                        }
                        enrollLauncher.launch(enrollIntent)
                    }
                    BiometricResult.AuthenticationSuccess -> {
                        viewModel.onAction(PinAction.OnBiometricsSuccess)
                    }
                    BiometricResult.FeatureUnavailable -> {
                        viewModel.onAction(
                            PinAction.OnBiometricsError(
                                UiText.DynamicString("Feature unavailable")
                            )
                        )
                    }
                    BiometricResult.HardwareUnavailable -> {
                        viewModel.onAction(
                            PinAction.OnBiometricsError(
                                UiText.DynamicString("Hardware unavailable")
                            )
                        )
                    }
                }
            }

            PinScreenRoot(
                viewModel = viewModel,
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToPreferences = {
                    navController.navigate(Routes.PreferencesScreen(PrefsFlow.AFTER_REGISTER)) {
                        popUpTo(Routes.AuthGraph) {
                            inclusive = true
                        }
                    }
                },
                onRefreshLogin = {
                    navController.navigateUp()
                },
                onBiometricLogin = {
                    activity?.promptManager
                        ?.showBiometricPrompt(
                            title = "Biometric Authentication",
                            description = "Log in using your biometric credential"
                        )
                }
            )
        }

        composable<Routes.LoginScreen> {
            val activity = LocalActivity.current as? MainActivity

            val viewModel: LoginViewModel = koinViewModel()

            val enrollLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = {
                    Timber.i("Activity result: $it")
                }
            )

            ObserveAsEvents(
                flow = activity?.promptManager?.promptResults ?: emptyFlow<BiometricResult>()
            ) { result ->
                when(result) {
                    is BiometricResult.AuthenticationError -> {
                        viewModel.onAction(
                            LoginAction.OnBiometricsError(
                                UiText.DynamicString(result.error),
                                false
                            )
                        )
                    }
                    BiometricResult.AuthenticationFailed -> {
                        Timber.e("Authentication failed")
                    }
                    BiometricResult.AuthenticationNotSet -> {
                        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                            putExtra(
                                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                            )
                        }
                        enrollLauncher.launch(enrollIntent)
                    }
                    BiometricResult.AuthenticationSuccess -> {
                        viewModel.onAction(LoginAction.OnBiometricsSuccess)
                    }
                    BiometricResult.FeatureUnavailable -> {
                        viewModel.onAction(
                            LoginAction.OnBiometricsError(
                                UiText.DynamicString("Feature unavailable"),
                                true
                            )
                        )
                    }
                    BiometricResult.HardwareUnavailable -> {
                        viewModel.onAction(
                            LoginAction.OnBiometricsError(
                                UiText.DynamicString("Hardware unavailable"),
                                true
                            )
                        )
                    }
                }
            }

            LoginScreenRoot(
                viewModel = viewModel,
                onNavigateToRegister = {
                    navController.navigate(Routes.RegisterScreen) {
                        popUpTo(Routes.AuthGraph) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Routes.DashboardGraph) {
                        popUpTo(Routes.AuthGraph) {
                            inclusive = true
                        }
                    }
                },
                onRequestBiometrics = {
                    activity?.promptManager
                        ?.showBiometricPrompt(
                            title = "Biometric Authentication",
                            description = "Log in using your biometric credential"
                        )
                }
            )
        }
    }
}

private fun NavGraphBuilder.dashboardGraph(
    navController: NavHostController
) {
    navigation<Routes.DashboardGraph>(
        startDestination = Routes.DashboardScreen,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) }
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
        startDestination = Routes.SettingsScreen,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) }
    ) {
        composable<Routes.SettingsScreen> {
            SettingsScreenRoot(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToPreferences = {
                    navController.navigate(Routes.PreferencesScreen(PrefsFlow.SETTINGS))
                },
                onNavigateToSecurity = {
                    navController.navigate(Routes.SecurityScreen)
                },
                onLogOut = {
                    navController.navigate(Routes.AuthGraph) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Routes.PreferencesScreen> {
            PreferencesScreenRoot(
                onNavigateToDashboard = {
                    navController.navigate(Routes.DashboardScreen) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }

        composable<Routes.SecurityScreen> {
            SecurityScreenRoot(
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}
