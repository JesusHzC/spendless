@file:OptIn(ExperimentalFoundationApi::class)

package com.jesushz.spendless.dashboard.presentation.all_transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.spendless.R
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessScaffold
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.dashboard.presentation.all_transactions.components.AllTransactionsTopBar
import com.jesushz.spendless.core.presentation.designsystem.components.TransactionItem
import com.jesushz.spendless.dashboard.presentation.create_transaction.CreateTransactionBottomSheetRoot
import com.jesushz.spendless.dashboard.presentation.dashboard.DashboardAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllTransactionsScreenRoot(
    viewModel: AllTransactionsViewModel = koinViewModel(),
    onNavigateUp: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AllTransactionsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                AllTransactionsAction.OnBack -> onNavigateUp()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun AllTransactionsScreen(
    state: AllTransactionsState,
    onAction: (AllTransactionsAction) -> Unit
) {
    CreateTransactionBottomSheetRoot(
        showBottomSheet = state.showCreateTransactionBottomSheet,
        transaction = state.tmpTransaction,
        onDismissRequest = {
            onAction(AllTransactionsAction.OnDismissTransactionClick)
        }
    )
    SpendLessScaffold(
        topBar = {
            AllTransactionsTopBar(
                onNavigateBack = {
                    onAction(AllTransactionsAction.OnBack)
                },
                onExportDataClick = {}
            )
        }
    ) { innerPadding ->
        when {
            state.allTransactions.isNotEmpty() -> {
                var itemSelected by remember { mutableStateOf<Transaction?>(null) }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(12.dp)
                ) {
                    state.comingSoonTransactions.fastForEach { transactions ->
                        stickyHeader {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = transactions.date,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.background
                                    )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(
                            items = transactions.transactions,
                            key = { it.hashCode() }
                        ) { transaction ->
                            TransactionItem(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                transaction = transaction,
                                itemSelected = itemSelected,
                                amountFormatted = state.formatAmount(transaction.amount),
                                onItemSelected = {
                                    itemSelected = transaction
                                },
                                showEditAction = false,
                                onItemEdit = {},
                                onItemDelete = {
                                    onAction(AllTransactionsAction.OnDeleteTransaction(transaction))
                                }
                            )
                        }
                    }
                    state.allTransactions.fastForEach { transactions ->
                        stickyHeader {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = transactions.date,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.background
                                    )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(
                            items = transactions.transactions,
                            key = { it.id }
                        ) { transaction ->
                            TransactionItem(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                transaction = transaction,
                                itemSelected = itemSelected,
                                amountFormatted = state.formatAmount(transaction.amount),
                                onItemSelected = {
                                    itemSelected = transaction
                                },
                                onItemEdit = {
                                    onAction(AllTransactionsAction.OnEditTransaction(transaction))
                                },
                                onItemDelete = {
                                    onAction(AllTransactionsAction.OnDeleteTransaction(transaction))
                                }
                            )
                        }
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo_spend),
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.no_transactions),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AllTransactionsScreenPreview() {
    SpendLessTheme {
        AllTransactionsScreen(
            state = AllTransactionsState(),
            onAction = {}
        )
    }
}
