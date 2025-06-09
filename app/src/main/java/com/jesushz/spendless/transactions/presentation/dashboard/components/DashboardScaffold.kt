package com.jesushz.spendless.transactions.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesushz.spendless.R
import com.jesushz.spendless.core.presentation.designsystem.theme.OnPrimaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme

@Composable
fun DashboardScaffold(
    title: String,
    onExportDataClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAddNewExpenseClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            DashboardTopBar(
                title = title,
                onExportDataClick = onExportDataClick,
                onSettingsClick = onSettingsClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNewExpenseClick,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 16.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_expense),
                )
            }
        },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        OnPrimaryFixed,
                        OnPrimaryFixed,
                    )
                )
            )
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Preview
@Composable
private fun DashboardScaffoldPreview() {
    SpendLessTheme {
        DashboardScaffold(
            title = "Dashboard",
            onExportDataClick = {},
            onSettingsClick = {},
            onAddNewExpenseClick = {},
            content = { }
        )
    }
}