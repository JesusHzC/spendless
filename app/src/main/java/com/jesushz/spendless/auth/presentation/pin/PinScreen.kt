package com.jesushz.spendless.auth.presentation.pin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessScaffold
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessTopBar
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme

@Composable
fun PinScreenRoot(
) {
    PinScreen()
}

@Composable
private fun PinScreen(
) {
    SpendLessScaffold(
        topBar = {
            SpendLessTopBar(
                onNavigateBack = {}
            )
        }
    ) { innerPadding ->

    }
}

@Preview
@Composable
private fun PinScreenPreview() {
    SpendLessTheme {
        PinScreen()
    }
}