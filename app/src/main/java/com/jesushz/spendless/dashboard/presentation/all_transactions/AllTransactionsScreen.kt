package com.jesushz.spendless.dashboard.presentation.all_transactions

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessScaffold
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.dashboard.presentation.all_transactions.components.AllTransactionsTopBar

@Composable
fun AllTransactionsScreenRoot() {
    AllTransactionsScreen()
}

@Composable
private fun AllTransactionsScreen() {
    SpendLessScaffold(
        topBar = {
            AllTransactionsTopBar(
                onNavigateBack = {},
                onExportDataClick = {}
            )
        }
    ) { innerPadding -> }
}

@Preview
@Composable
private fun AllTransactionsScreenPreview() {
    SpendLessTheme {
        AllTransactionsScreen()
    }
}
