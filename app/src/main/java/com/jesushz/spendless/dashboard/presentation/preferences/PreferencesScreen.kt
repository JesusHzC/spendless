package com.jesushz.spendless.dashboard.presentation.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jesushz.spendless.R
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessButton
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessScaffold
import com.jesushz.spendless.core.presentation.designsystem.components.SpendLessTopBar
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.presentation.ui.ObserveAsEvents
import com.jesushz.spendless.core.domain.Currency
import com.jesushz.spendless.core.domain.DecimalSeparator
import com.jesushz.spendless.core.domain.ExpenseFormat
import com.jesushz.spendless.core.domain.ThousandSeparator
import org.koin.androidx.compose.koinViewModel

@Composable
fun PreferencesScreenRoot(
    viewModel: PreferencesViewModel = koinViewModel(),
    onNavigateToDashboard: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ObserveAsEvents(
        flow = viewModel.event,
    ) { event ->
        when (event) {
            PreferencesEvent.OnSavePreferencesSuccess -> onNavigateToDashboard()
        }
    }
    PreferencesScreen(
        state = state,
        onAction = { action ->
            when (action) {
                PreferencesAction.OnNavigateBack -> onNavigateToDashboard()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun PreferencesScreen(
    state: PreferencesState,
    onAction: (PreferencesAction) -> Unit
) {
    SpendLessScaffold(
        topBar = {
            SpendLessTopBar(
                onNavigateBack = {
                    onAction(PreferencesAction.OnNavigateBack)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.preferences_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.preferences_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            MonthlySpendCard(spendValue = state.totalSpendFormat)
            Spacer(modifier = Modifier.height(20.dp))
            ExpenseFormatSelector(
                expenseFormatSelected = state.expenseFormat,
                onExpenseFormatSelected = { format ->
                    onAction(PreferencesAction.OnExpenseFormatSelected(format))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CurrencySelector(
                currencySelected = state.currency,
                onCurrencySelected = { currency ->
                    onAction(PreferencesAction.OnCurrencySelected(currency))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DecimalSeparatorSelector(
                decimalSeparatorSelected = state.decimalSeparator,
                onDecimalSeparatorSelected = { separator ->
                    onAction(PreferencesAction.OnDecimalSeparatorSelected(separator))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ThousandsSeparatorSelector(
                thousandsSeparatorSelected = state.thousandSeparator,
                onThousandsSeparatorSelected = { separator ->
                    onAction(PreferencesAction.OnThousandSeparatorSelected(separator))
                }
            )
            Spacer(modifier = Modifier.height(34.dp))
            SpendLessButton(
                onButtonClick = {
                    onAction(PreferencesAction.OnStartTrackingClick)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.start_tracking),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun MonthlySpendCard(
    spendValue: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = spendValue,
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.monthly_spend),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
            )
        }
    }
}

@Composable
private fun ExpenseFormatSelector(
    expenseFormatSelected: ExpenseFormat,
    onExpenseFormatSelected: (ExpenseFormat) -> Unit
) {
    Text(
        text = stringResource(R.string.expense_format),
        style = MaterialTheme.typography.labelSmall
    )
    Spacer(modifier = Modifier.height(4.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            ExpenseFormat.entries.fastForEach { format ->
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = if (expenseFormatSelected == format)
                            MaterialTheme.colorScheme.onPrimary
                        else Color.Transparent
                    ),
                    onClick = {
                        onExpenseFormatSelected(format)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = format.value,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencySelector(
    currencySelected: Currency,
    onCurrencySelected: (Currency) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var menuMaxWidth by remember {
        mutableIntStateOf(0)
    }
    val menuMaxWidthDp = with(LocalDensity.current) { menuMaxWidth.toDp() }

    Text(
        text = stringResource(R.string.currency),
        style = MaterialTheme.typography.labelSmall
    )
    Spacer(modifier = Modifier.height(4.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                menuMaxWidth = maxOf(menuMaxWidth, size.width)
            }
            .clickable {
                isExpanded = true
            }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = currencySelected.value,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .width(menuMaxWidthDp),
            containerColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Currency.entries.fastForEach { currency ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = currency.value,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        isExpanded = false
                        onCurrencySelected(currency)
                    },
                )
            }
        }
    }
}

@Composable
private fun DecimalSeparatorSelector(
    decimalSeparatorSelected: DecimalSeparator,
    onDecimalSeparatorSelected: (DecimalSeparator) -> Unit
) {
    Text(
        text = stringResource(R.string.decimal_separator),
        style = MaterialTheme.typography.labelSmall
    )
    Spacer(modifier = Modifier.height(4.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            DecimalSeparator.entries.fastForEach { format ->
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = if (decimalSeparatorSelected == format)
                            MaterialTheme.colorScheme.onPrimary
                        else Color.Transparent
                    ),
                    onClick = {
                        onDecimalSeparatorSelected(format)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = format.value,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThousandsSeparatorSelector(
    thousandsSeparatorSelected: ThousandSeparator,
    onThousandsSeparatorSelected: (ThousandSeparator) -> Unit
) {
    Text(
        text = stringResource(R.string.thousands_separator),
        style = MaterialTheme.typography.labelSmall
    )
    Spacer(modifier = Modifier.height(4.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            ThousandSeparator.entries.fastForEach { format ->
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = if (thousandsSeparatorSelected == format)
                            MaterialTheme.colorScheme.onPrimary
                        else Color.Transparent
                    ),
                    onClick = {
                        onThousandsSeparatorSelected(format)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = format.value,
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
private fun PreferencesScreenPreview() {
    SpendLessTheme {
        val state = PreferencesState()
        PreferencesScreen(
            state = state.copy(
                totalSpendFormat = state.formatAmount()
            ),
            onAction = {}
        )
    }
}