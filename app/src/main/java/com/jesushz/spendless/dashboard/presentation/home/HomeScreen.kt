@file:OptIn(ExperimentalFoundationApi::class)

package com.jesushz.spendless.dashboard.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.spendless.R
import com.jesushz.spendless.core.database.entity.TransactionEntity
import com.jesushz.spendless.core.domain.transactions.TransactionType
import com.jesushz.spendless.core.presentation.designsystem.theme.PrimaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.SecondaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.util.formatToReadableDate
import com.jesushz.spendless.dashboard.presentation.components.TransactionItem
import com.jesushz.spendless.dashboard.presentation.create_transaction.CreateTransactionBottomSheetRoot
import com.jesushz.spendless.dashboard.presentation.home.components.DashboardScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToShowAllTransactions: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                HomAction.OnShowAllTransactions -> {
                    onNavigateToShowAllTransactions()
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun HomeScreen(
    state: HomeState,
    onAction: (HomAction) -> Unit
) {
    CreateTransactionBottomSheetRoot(
        showBottomSheet = state.showCreateTransactionBottomSheet,
        onDismissRequest = {
            onAction(HomAction.OnDismissTransactionClick)
        }
    )
    DashboardScaffold(
        title = state.username,
        onExportDataClick = {},
        onSettingsClick = {},
        onAddNewExpenseClick = {
            onAction(HomAction.OnCreateTransactionClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .padding(16.dp)
            ) {
                AccountBalance(
                    accountBalance = state.formatAmount(state.accountBalance),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Statistics(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    state = state
                )
            }
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
            ) {
                LatestTransactions(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    state = state,
                    onShowAllTransactions = {
                        onAction(HomAction.OnShowAllTransactions)
                    }
                )
            }
        }
    }
}

@Composable
private fun LatestTransactions(
    modifier: Modifier = Modifier,
    state: HomeState,
    onShowAllTransactions: () -> Unit
) {
    var itemSelected by remember { mutableStateOf<TransactionEntity?>(null) }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.latest_transactions),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(
                    onClick = onShowAllTransactions
                ) {
                    Text(
                        text = stringResource(R.string.show_all),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        when {
            state.latestTransactions.isNotEmpty() -> {
                state.latestTransactions.fastForEach { transactions ->
                    if (transactions.transactions.isNotEmpty()) {
                        stickyHeader {
                            Text(
                                text = stringResource(transactions.title),
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
                                }
                            )
                        }
                    }
                }
            }
            else -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .fillParentMaxHeight(0.8f),
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
}



@Composable
private fun Statistics(
    modifier: Modifier = Modifier,
    state: HomeState,
) {
    Column(
        modifier = modifier,
    ) {
        state.latestTransaction?.let { transaction ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary.copy(
                        alpha = 0.2f
                    ),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .size(56.dp)
                            .background(
                                color = if (transaction.transactionType == TransactionType.INCOME) {
                                    SecondaryFixed.copy(alpha = 0.9f)
                                } else {
                                    PrimaryFixed
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (transaction.transactionType == TransactionType.INCOME) {
                                "\uD83D\uDCB0"
                            } else {
                                transaction.category?.icon.orEmpty()
                            },
                            fontSize = 30.sp
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = transaction.receiver,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = if (transaction.transactionType == TransactionType.INCOME) {
                                stringResource(R.string.income)
                            } else {
                                transaction.category?.title.orEmpty()
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            state.longestTransaction?.let { transaction ->
                Card(
                    modifier = Modifier
                        .weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = PrimaryFixed,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = transaction.receiver,
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Text(
                                text = stringResource(R.string.longest_transaction),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp
                                ),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column(
                            modifier = Modifier
                                .weight(0.7f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.formatAmount(transaction.amount),
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Text(
                                text = formatToReadableDate(transaction.dateTime),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp
                                ),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Card(
                modifier = Modifier
                    .weight(0.5f),
                colors = CardDefaults.cardColors(
                    containerColor = SecondaryFixed,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.formatAmount(state.previousWeekBalance),
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = stringResource(R.string.previous_week_balance),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountBalance(
    modifier: Modifier = Modifier,
    accountBalance: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = accountBalance,
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.account_balance),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    SpendLessTheme {
        HomeScreen(
            state = HomeState(),
            onAction = {}
        )
    }
}