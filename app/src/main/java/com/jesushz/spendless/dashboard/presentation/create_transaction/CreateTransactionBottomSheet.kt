@file:OptIn(ExperimentalMaterial3Api::class)

package com.jesushz.spendless.dashboard.presentation.create_transaction

import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.spendless.R
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.presentation.designsystem.theme.OnPrimaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.presentation.designsystem.theme.SurfaceContainerLow
import com.jesushz.spendless.core.domain.transactions.TransactionType
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessButton
import com.jesushz.spendless.core.presentation.ui.ObserveAsEvents
import com.jesushz.spendless.dashboard.presentation.create_transaction.components.AmountTextField
import com.jesushz.spendless.dashboard.presentation.create_transaction.components.DateSelector
import com.jesushz.spendless.dashboard.presentation.create_transaction.components.DropDownCategories
import com.jesushz.spendless.dashboard.presentation.create_transaction.components.DropDownRepeat
import com.jesushz.spendless.dashboard.presentation.create_transaction.components.NoteTextField
import com.jesushz.spendless.dashboard.presentation.create_transaction.components.ReceiverTextField
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateTransactionBottomSheetRoot(
    viewModel: CreateTransactionViewModel = koinViewModel(),
    showBottomSheet: Boolean,
    transaction: Transaction? = null,
    onDismissRequest: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(transaction) {
        if (transaction != null) {
            viewModel.onAction(CreateTransactionAction.OnUpdateTransaction(transaction))
        }
    }

    ObserveAsEvents(
        flow = viewModel.event
    ) { event ->
        when (event) {
            CreateTransactionEvent.OnCreateTransactionSuccess -> {
                scope.launch {
                    sheetState.hide()
                    onDismissRequest()
                }
            }
            is CreateTransactionEvent.OnError -> {
                Toast
                    .makeText(
                        context,
                        event.error.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
    }

    if (showBottomSheet) {
        CreateTransactionBottomSheet(
            sheetState = sheetState,
            state = state,
            onAction = { action ->
                viewModel.onAction(action)
                when (action) {
                    CreateTransactionAction.OnClose -> {
                        scope.launch {
                            sheetState.hide()
                            onDismissRequest()
                        }
                    }
                    else -> Unit
                }
            }
        )
    }
}

@Composable
private fun CreateTransactionBottomSheet(
    sheetState: SheetState,
    state: CreateTransactionState,
    onAction: (CreateTransactionAction) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            onAction(CreateTransactionAction.OnClose)
        },
        sheetState = sheetState,
        containerColor = SurfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .systemBarsPadding()
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
                    text = stringResource(R.string.create_transactions),
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(
                    onClick = {
                        onAction(CreateTransactionAction.OnClose)
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
            TransactionTypeSelector(
                modifier = Modifier
                    .fillMaxWidth(),
                transactionTypeSelected = state.transactionType,
                onTransactionTypeSelected = {
                    onAction(CreateTransactionAction.OnTransactionTypeSelected(it))
                }
            )
            Spacer(modifier = Modifier.height(34.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReceiverTextField(
                    receiver = state.receiver,
                    onReceiverChange = {
                        onAction(CreateTransactionAction.OnReceiverChange(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                AmountTextField(
                    amount = state.amount,
                    expenseFormat = state.expenseFormat,
                    transactionType = state.transactionType,
                    onAmountChange = {
                        onAction(CreateTransactionAction.OnAmountChange(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                NoteTextField(
                    note = state.note,
                    onNoteChange = {
                        onAction(CreateTransactionAction.OnNoteChange(it))
                    },
                )
            }
            Spacer(modifier = Modifier.height(34.dp))
            DateSelector(
                modifier = Modifier.fillMaxWidth(),
                dateSelected = state.dateSelected,
                onDateSelected = {
                    onAction(CreateTransactionAction.OnDateSelected(it))
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (state.transactionType == TransactionType.EXPENSE) {
                DropDownCategories(
                    modifier = Modifier
                        .fillMaxWidth(),
                    categorySelected = state.categorySelected,
                    onCategorySelected = {
                        onAction(CreateTransactionAction.OnCategorySelected(it))
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            DropDownRepeat(
                modifier = Modifier
                    .fillMaxWidth(),
                repeatSelected = state.repeatSelected,
                onRepeatTypeSelected = {
                    onAction(CreateTransactionAction.OnRepeatSelected(it))
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            SpendLessButton(
                onButtonClick = {
                    onAction(CreateTransactionAction.OnCreateTransactionClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.create_transaction),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun TransactionTypeSelector(
    modifier: Modifier,
    transactionTypeSelected: TransactionType,
    onTransactionTypeSelected: (TransactionType) -> Unit
) {
    Card(
        modifier = modifier,
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
            TransactionType.entries.fastForEach { transaction ->
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = if (transactionTypeSelected == transaction)
                            MaterialTheme.colorScheme.onPrimary
                        else Color.Transparent,
                        contentColor = if (transactionTypeSelected == transaction) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            OnPrimaryFixed
                        }
                    ),
                    onClick = {
                        onTransactionTypeSelected(transaction)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            painter = painterResource(transaction.icon),
                            contentDescription = transaction.title,
                        )
                        Text(
                            text = transaction.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreateTransactionBottomSheetPreview() {
    SpendLessTheme {
        CreateTransactionBottomSheet(
            state = CreateTransactionState(),
            sheetState = SheetState(
                skipPartiallyExpanded = false,
                density = LocalDensity.current,
                initialValue = SheetValue.Expanded
            ),
            onAction = {}
        )
    }
}
