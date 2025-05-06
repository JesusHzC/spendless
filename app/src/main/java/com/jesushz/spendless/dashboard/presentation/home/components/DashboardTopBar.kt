@file:OptIn(ExperimentalMaterial3Api::class)

package com.jesushz.spendless.dashboard.presentation.home.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesushz.spendless.R
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme

@Composable
fun DashboardTopBar(
    title: String,
    onExportDataClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        actions = {
            FilledIconButton(
                onClick = onExportDataClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.12f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = stringResource(R.string.export_data),
                )
            }
            FilledIconButton(
                onClick = onSettingsClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.12f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings),
                )
            }
        }
    )
}

@Preview
@Composable
private fun DashboardTopBarPreview() {
    SpendLessTheme {
        DashboardTopBar(
            title = "Dashboard",
            onExportDataClick = {},
            onSettingsClick = {}
        )
    }
}