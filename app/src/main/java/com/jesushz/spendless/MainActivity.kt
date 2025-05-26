package com.jesushz.spendless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.presentation.ui.ObserveAsEvents
import com.jesushz.spendless.core.util.Routes
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendLessTheme {
                val navController = rememberNavController()
                val state by viewModel.state.collectAsStateWithLifecycle()

                ObserveAsEvents(
                    flow = viewModel.event
                ) { event ->
                    when (event) {
                        MainEvent.OnNavigateToAuth -> {
                            navController.navigate(Routes.AuthGraph) {
                                popUpTo(0)
                                launchSingleTop = true
                            }
                        }
                        MainEvent.OnNavigateToPin -> {
                            navController.navigate(
                                Routes.PinScreen(
                                    flow = PinFlow.REFRESH_LOGIN,
                                    username = state.user?.username.orEmpty()
                                )
                            )
                        }
                    }
                }

                NavigationRoot(
                    navController = navController,
                    startDestination = if (state.isLoggedIn) {
                        Routes.DashboardGraph
                    } else {
                        Routes.AuthGraph
                    }
                )
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        viewModel.onUserInteraction()
    }

}
