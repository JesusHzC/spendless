package com.jesushz.spendless

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.jesushz.spendless.auth.domain.PinFlow
import com.jesushz.spendless.core.data.security.BiometricPromptManager
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.presentation.ui.ObserveAsEvents
import com.jesushz.spendless.core.util.Routes
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    internal val promptManager by lazy {
        BiometricPromptManager(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.endIsLoggedIn()
        enableEdgeToEdge()
        setContent {
            SpendLessTheme {
                val navController = rememberNavController()

                NotificationPermission()

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
                                    username = ""
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

    @Composable
    private fun NotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val factory = rememberPermissionsControllerFactory()
            val controller = remember(factory) {
                factory.createPermissionsController()
            }

            BindEffect(controller)

            val viewModelPermissions = androidx.lifecycle.viewmodel.compose.viewModel {
                PermissionsViewModel(controller)
            }

            when (viewModelPermissions.state) {
                PermissionState.Granted -> Timber.i("Granted")
                PermissionState.DeniedAlways -> Timber.i("Denied always")
                else -> viewModelPermissions.provideOrRequestNotificationPermission()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        viewModel.onUserInteraction()
        return super.dispatchTouchEvent(ev)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        viewModel.onUserInteraction()
        return super.dispatchKeyEvent(event)
    }

    override fun onUserInteraction() {
        viewModel.onUserInteraction()
        super.onUserInteraction()
    }

}
