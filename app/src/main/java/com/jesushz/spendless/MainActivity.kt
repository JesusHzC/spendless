package com.jesushz.spendless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.navigation.compose.rememberNavController
import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.core.domain.preferences.DataStoreManager
import com.jesushz.spendless.core.domain.security.SessionManager
import com.jesushz.spendless.core.presentation.designsystem.components.ComposableLifecycle
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.util.Routes
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class MainActivity : ComponentActivity(), KoinComponent {

    private val dataStoreManager by inject<DataStoreManager>()
    private val applicationScope by inject<CoroutineScope>()

    private val viewModel: MainViewModel by viewModel()

    private lateinit var sessionManager: SessionManager
    private var isSessionManagerPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendLessTheme {
                val navController = rememberNavController()

                ComposableLifecycle { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_CREATE -> {
                            Timber.i("ON_CREATE")
                            sessionManager = SessionManager(
                                dataStoreManager = dataStoreManager,
                                applicationScope = applicationScope,
                                onSessionExpired = {
                                    isSessionManagerPaused = true
                                    navController.navigate(Routes.AuthGraph) {
                                        popUpTo(0)
                                        launchSingleTop = true
                                    }
                                },
                                onLockOut = {
                                    isSessionManagerPaused = true
                                    navController.navigate(Routes.PinScreen(flow = PinFlow.REFRESH_LOGIN))
                                }
                            )
                        }
                        Lifecycle.Event.ON_RESUME -> {
                            Timber.i("ON_RESUME")
                            if (::sessionManager.isInitialized && !isSessionManagerPaused) {
                                sessionManager.start()
                            }
                        }
                        Lifecycle.Event.ON_DESTROY -> {
                            Timber.i("ON_DESTROY")
                            if (::sessionManager.isInitialized) {
                                sessionManager.stop()
                            }
                        }
                        else -> Unit
                    }
                }

                NavigationRoot(
                    navController = navController,
                    startDestination = if (viewModel.state.isLoggedIn) {
                        Routes.DashboardGraph
                    } else {
                        Routes.AuthGraph
                    }
                )
            }
        }
    }

    fun updateIsSessionManagerPaused(isPaused: Boolean) {
        isSessionManagerPaused = isPaused
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        Timber.i("onUserInteraction")
        if (::sessionManager.isInitialized) {
            sessionManager.touch()
            if (!isSessionManagerPaused) {
                sessionManager.start()
            }
        }
    }

}
