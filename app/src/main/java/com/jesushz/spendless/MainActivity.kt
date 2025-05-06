package com.jesushz.spendless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.util.Routes
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendLessTheme {
                val viewModel: MainViewModel = koinViewModel()
                val navController = rememberNavController()
                NavigationRoot(
                    navController = navController,
                    startDestination = if (viewModel.state.isLoggedIn) {
                        Routes.DashboardGraph
                    } else {
                        Routes.DashboardGraph
                    }
                )
            }
        }
    }
}
