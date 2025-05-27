package com.jesushz.spendless

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.core.data.security.BiometricPromptManager
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.presentation.ui.ObserveAsEvents
import com.jesushz.spendless.core.util.Routes
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    internal val promptManager by lazy {
        BiometricPromptManager(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.logout()
        }
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
                    navController = navController
                )
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        viewModel.onUserInteraction()
    }

}
