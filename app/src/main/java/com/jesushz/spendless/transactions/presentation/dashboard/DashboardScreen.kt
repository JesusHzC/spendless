@file:OptIn(ExperimentalFoundationApi::class)

package com.jesushz.spendless.transactions.presentation.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.spendless.R
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionType
import com.jesushz.spendless.core.presentation.designsystem.theme.PrimaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.SecondaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.util.formatToReadableDate
import com.jesushz.spendless.core.presentation.designsystem.components.TransactionItem
import com.jesushz.spendless.core.util.mask
import com.jesushz.spendless.transactions.presentation.create_transaction.CreateTransactionBottomSheetRoot
import com.jesushz.spendless.transactions.presentation.dashboard.components.DashboardScaffold
import com.jesushz.spendless.transactions.presentation.export_transactions.ExportTransactionsBottomSheetRoot
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreenRoot(
    viewModel: DashboardViewModel = koinViewModel(),
    onNavigateToShowAllTransactions: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    DashboardScreen(
        state = state,
        snackBarHostState = snackBarHostState,
        onAction = { action ->
            when (action) {
                DashboardAction.OnShowAllTransactions -> {
                    onNavigateToShowAllTransactions()
                }
                DashboardAction.OnSettingsClick -> {
                    onNavigateToSettings()
                }
                is DashboardAction.OnError -> {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            message = action.error.asString(context)
                        )
                    }
                }
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun DashboardScreen(
    state: DashboardState,
    snackBarHostState: SnackbarHostState,
    onAction: (DashboardAction) -> Unit
) {
    val stateList = rememberLazyListState()

    val scrollOffset = remember {
        derivedStateOf {
            stateList.firstVisibleItemScrollOffset + stateList.firstVisibleItemIndex * 100
        }
    }

    val progress by remember {
        derivedStateOf {
            (scrollOffset.value / 150f).coerceIn(0f, 1f)
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 200),
        label = "animatedProgress"
    )

    val dynamicWeight by remember {
        derivedStateOf {
            lerp(0.7f, 0.25f, animatedProgress)
        }
    }

    val dynamicWeightStatistics by remember {
        derivedStateOf {
            lerp(1f, 0.01f, animatedProgress)
        }
    }

    val scale by remember {
        derivedStateOf {
            lerp(1f, 0f, animatedProgress)
        }
    }

    BackHandler { Unit }

    CreateTransactionBottomSheetRoot(
        showBottomSheet = state.showCreateTransactionBottomSheet,
        transaction = state.tmpTransaction,
        onDismissRequest = {
            onAction(DashboardAction.OnDismissTransactionClick)
        },
        onError = {
            onAction(DashboardAction.OnError(it))
        }
    )
    ExportTransactionsBottomSheetRoot(
        showBottomSheet = state.showExportTransactionBottomSheet,
        onDismissRequest = {
            onAction(DashboardAction.OnDismissExportTransactionBottomSheet)
        },
        onError = { error ->
            onAction(DashboardAction.OnError(error))
        }
    )
    DashboardScaffold(
        title = state.username,
        snackBarHost = snackBarHostState,
        onExportDataClick = {
            onAction(DashboardAction.OnExportTransactionClick)
        },
        onSettingsClick = {
            onAction(DashboardAction.OnSettingsClick)
        },
        onAddNewExpenseClick = {
            onAction(DashboardAction.OnCreateTransactionClick)
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
                    .weight(dynamicWeight)
                    .padding(16.dp)
            ) {
                AccountBalance(
                    accountBalance = state.formatAmount(state.accountBalance),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    showBalance = state.shouldShowBalance,
                    onShowBalanceClick = {
                        onAction(DashboardAction.OnShowBalanceClick)
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(dynamicWeightStatistics)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            alpha = scale
                        }
                ) {
                    Statistics(
                        modifier = Modifier.fillMaxSize(),
                        state = state
                    )
                }
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
                    stateList = stateList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    state = state,
                    onShowAllTransactions = {
                        onAction(DashboardAction.OnShowAllTransactions)
                    },
                    onEditTransaction = { transaction ->
                        onAction(DashboardAction.OnEditTransaction(transaction))
                    },
                    onDeleteTransaction = { transaction ->
                        onAction(DashboardAction.OnDeleteTransaction(transaction))
                    }
                )
            }
        }
    }
}

@Composable
private fun LatestTransactions(
    modifier: Modifier = Modifier,
    stateList: LazyListState,
    state: DashboardState,
    onShowAllTransactions: () -> Unit,
    onEditTransaction: (Transaction) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit
) {
    var itemSelected by remember { mutableStateOf<Transaction?>(null) }
    LazyColumn(
        state = stateList,
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
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = MaterialTheme.colorScheme.surface
                                    )
                            ) {
                                Text(
                                    text = transactions.date,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                )
                            }
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
                                    onEditTransaction(transaction)
                                },
                                onItemDelete = {
                                    onDeleteTransaction(transaction)
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
    state: DashboardState,
) {
    Column(
        modifier = modifier,
    ) {
        state.latestTransaction?.let { transaction ->
            Card(
                modifier = Modifier
                    .weight(1f)
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
                .weight(1f)
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
                                text = formatToReadableDate(transaction.date),
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
    showBalance: Boolean = true,
    accountBalance: String,
    onShowBalanceClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showBalance) {
            Text(
                text = accountBalance,
                style = MaterialTheme.typography.displayLarge
            )
        } else {
            Text(
                text = accountBalance.mask(),
                style = MaterialTheme.typography.displaySmall
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.account_balance),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onShowBalanceClick,
                modifier = Modifier
                    .size(14.dp)
            ) {
                Icon(
                    painter = if (showBalance) {
                        painterResource(R.drawable.ic_hidde)
                    } else {
                        painterResource(R.drawable.ic_show)
                    },
                    contentDescription = stringResource(R.string.show_balance)
                )
            }
        }
    }
}

@Preview
@Composable
private fun DashboardScreenPreview() {
    SpendLessTheme {
        DashboardScreen(
            state = DashboardState(),
            snackBarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}