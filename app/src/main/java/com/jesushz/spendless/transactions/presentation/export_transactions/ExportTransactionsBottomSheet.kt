@file:OptIn(ExperimentalMaterial3Api::class)

package com.jesushz.spendless.transactions.presentation.export_transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.spendless.R
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessButton
import com.jesushz.spendless.core.presentation.designsystem.theme.SurfaceContainerLow
import com.jesushz.spendless.core.presentation.ui.ObserveAsEvents
import com.jesushz.spendless.core.presentation.ui.UiText
import com.jesushz.spendless.core.util.userInteraction
import com.jesushz.spendless.transactions.presentation.export_transactions.components.DropDownExport
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExportTransactionsBottomSheetRoot(
    viewModel: ExportTransactionsViewModel = koinViewModel(),
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    onError: (UiText) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ObserveAsEvents(
        flow = viewModel.event,
    ) { event ->
        when (event) {
            is ExportTransactionsEvent.OnExportError -> {
                onError(event.error)
            }
            ExportTransactionsEvent.OnExportSuccess -> {
                scope.launch {
                    sheetState.hide()
                    onDismissRequest()
                }
            }
        }
    }

    if (showBottomSheet) {
        ExportTransactionsBottomSheet(
            sheetState = sheetState,
            state = state,
            onAction = { action ->
                when (action) {
                    ExportTransactionsAction.OnClose -> onDismissRequest()
                    else -> viewModel.onAction(action)
                }
            }
        )
    }
}

@Composable
private fun ExportTransactionsBottomSheet(
    sheetState: SheetState,
    state: ExportTransactionsState,
    onAction: (ExportTransactionsAction) -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onAction(ExportTransactionsAction.OnClose)
        },
        containerColor = SurfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .systemBarsPadding()
            .userInteraction()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.export_transactions),
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(
                    onClick = {
                        onAction(ExportTransactionsAction.OnClose)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            DropDownExport(
                modifier = Modifier.fillMaxWidth(),
                exportSelected = state.exportSelected,
                onExportSelected = { export ->
                    onAction(ExportTransactionsAction.OnExportSelected(export))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SpendLessButton(
                onButtonClick = {
                    onAction(ExportTransactionsAction.OnExportTransactions)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.export_transactions),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
