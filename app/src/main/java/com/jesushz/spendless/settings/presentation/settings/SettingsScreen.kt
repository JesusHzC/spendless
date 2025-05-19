package com.jesushz.spendless.settings.presentation.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jesushz.spendless.R
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessScaffold
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessTopBar
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme

@Composable
fun SettingsScreenRoot(
    onNavigateUp: () -> Unit,
    onNavigateToPreferences: () -> Unit,
    onNavigateToSecurity: () -> Unit
) {
    SettingsScreen(
        onAction = { action ->
            when (action) {
                SettingsAction.OnLogOutClick -> {
                }
                SettingsAction.OnPreferencesClick -> {
                    onNavigateToPreferences()
                }
                SettingsAction.OnSecurityClick -> {
                }
                SettingsAction.OnBackClick -> {
                    onNavigateUp()
                }
            }
        }
    )
}

@Composable
private fun SettingsScreen(
    onAction: (SettingsAction) -> Unit
) {
    SpendLessScaffold(
        topBar = {
            SpendLessTopBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                onNavigateBack = {
                    onAction(SettingsAction.OnBackClick)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    MenuItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAction(SettingsAction.OnPreferencesClick)
                            },
                        icon = R.drawable.ic_settings,
                        title = stringResource(R.string.preferences)
                    )
                    MenuItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAction(SettingsAction.OnSecurityClick)
                            },
                        icon = R.drawable.ic_security,
                        title = stringResource(R.string.security)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                MenuItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            onAction(SettingsAction.OnLogOutClick)
                        },
                    icon = R.drawable.ic_security,
                    title = stringResource(R.string.log_out),
                    itemColor = MaterialTheme.colorScheme.error,
                    backgroundIconColor = MaterialTheme.colorScheme.errorContainer
                )
            }
        }
    }
}

@Composable
private fun MenuItem(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    title: String,
    backgroundIconColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    itemColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(40.dp)
                .background(backgroundIconColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(16.dp),
                tint = itemColor
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(1f),
            color = itemColor
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SpendLessTheme {
        SettingsScreen(
            onAction = {}
        )
    }
}
