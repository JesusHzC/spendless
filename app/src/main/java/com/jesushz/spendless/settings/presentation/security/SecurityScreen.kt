package com.jesushz.spendless.settings.presentation.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.jesushz.spendless.R
import com.jesushz.spendless.core.domain.security.Biometrics
import com.jesushz.spendless.core.domain.security.LockedOutDuration
import com.jesushz.spendless.core.domain.security.SessionDuration
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessScaffold
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessTopBar
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme

@Composable
fun SecurityScreenRoot(
    onNavigateUp: () -> Unit
) {
    SecurityScreen()
}

@Composable
private fun SecurityScreen() {
    SpendLessScaffold(
        topBar = {
            SpendLessTopBar(
                title = {
                    Text(
                        text = stringResource(R.string.security),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                onNavigateBack = {

                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            BaseSelector(
                title = stringResource(R.string.biometrics_for_pin_prompt),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Biometrics.entries.fastForEach { biometricOption ->
                    BaseSelectorItem(
                        modifier = Modifier
                            .weight(1f),
                        item = biometricOption,
                        onItemSelected = {

                        },
                        title = biometricOption.title
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            BaseSelector(
                title = stringResource(R.string.session_duration),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SessionDuration.entries.fastForEach { duration ->
                    BaseSelectorItem(
                        modifier = Modifier
                            .weight(1f),
                        item = duration,
                        onItemSelected = {

                        },
                        title = duration.title
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            BaseSelector(
                title = stringResource(R.string.locked_out_duration),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                LockedOutDuration.entries.fastForEach { duration ->
                    BaseSelectorItem(
                        modifier = Modifier
                            .weight(1f),
                        item = duration,
                        onItemSelected = {

                        },
                        title = duration.title
                    )
                }
            }
        }
    }
}

@Composable
private fun <T> BaseSelectorItem(
    modifier: Modifier = Modifier,
    itemIsSelected: Boolean = false,
    item: T,
    onItemSelected: (T) -> Unit,
    title: String,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (itemIsSelected)
                MaterialTheme.colorScheme.onPrimary
            else Color.Transparent
        ),
        onClick = {
            onItemSelected(item)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun BaseSelector(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable RowScope.() -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun SecurityScreenPreview() {
    SpendLessTheme {
        SecurityScreen()
    }
}