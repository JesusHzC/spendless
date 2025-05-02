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
                onNavigateToPin = {
                    navController.navigate(Routes.PinScreen(PinFlow.REGISTER))
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
                }
            )
        }

        composable<Routes.LoginScreen> {
            LoginScreenRoot(
                onNavigateToRegister = {
                    navController.navigate(Routes.RegisterScreen)
                }
            )
        }
    }
}
