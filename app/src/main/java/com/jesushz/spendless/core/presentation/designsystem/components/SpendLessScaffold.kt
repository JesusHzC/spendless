package com.jesushz.spendless.core.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpendLessScaffold(
    topBar: @Composable () -> Unit = {},
    snackBarHost: SnackbarHostState? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBar,
        snackbarHost = {
            snackBarHost?.let { state ->
                SnackbarHost(
                    hostState = state,
                    snackbar = { data ->
                        SpendLessSnackBar(
                            data = data,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .padding(top = 12.dp, bottom = 36.dp)
                        )
                    }
                )
            }
        },
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        content(innerPadding)
    }
}
