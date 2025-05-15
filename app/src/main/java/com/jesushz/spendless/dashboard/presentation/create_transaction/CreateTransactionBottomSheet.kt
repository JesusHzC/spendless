@file:OptIn(ExperimentalMaterial3Api::class)

package com.jesushz.spendless.dashboard.presentation.create_transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jesushz.spendless.R
import com.jesushz.spendless.core.presentation.designsystem.theme.OnPrimaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.PrimaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.presentation.designsystem.theme.SurfaceContainerLow
import com.jesushz.spendless.dashboard.domain.Category
import com.jesushz.spendless.dashboard.domain.Repeat
import com.jesushz.spendless.dashboard.domain.TransactionType
import com.jesushz.spendless.dashboard.presentation.create_transaction.components.AmountTextField
import com.jesushz.spendless.dashboard.presentation.create_transaction.components.NoteTextField
import com.jesushz.spendless.dashboard.presentation.create_transaction.components.ReceiverTextField
import kotlinx.coroutines.launch

@Composable
fun CreateTransactionBottomSheetRoot(
    viewModel: CreateTransactionViewModel = viewModel(),
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        CreateTransactionBottomSheet(
            sheetState = sheetState,
            state = state,
            onAction = { action -> 
                when (action) {
                    CreateTransactionAction.OnClose -> {
                        scope.launch {
                            sheetState.hide()
                            onDismissRequest()
                        }
                    }
                    else -> viewModel.onAction(action)
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
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                    TransactionType.entries.fastForEach { transaction ->
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = if (state.transactionType == transaction)
                                    MaterialTheme.colorScheme.onPrimary
                                else Color.Transparent,
                                contentColor = if (state.transactionType == transaction) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    OnPrimaryFixed
                                }
                            ),
                            onClick = {
                                onAction(CreateTransactionAction.OnTransactionTypeSelected(transaction))
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
                AmountTextField(
                    amount = state.amount,
                    onAmountChange = {
                        onAction(CreateTransactionAction.OnAmountChange(it))
                    }
                )
                NoteTextField(
                    note = state.note,
                    onNoteChange = {
                        onAction(CreateTransactionAction.OnNoteChange(it))
                    },
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            DropDownCategories(
                modifier = Modifier
                    .fillMaxWidth(),
                categorySelected = state.categorySelected,
                onCategorySelected = {
                    onAction(CreateTransactionAction.OnCategorySelected(it))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            DropDownRepeat(
                modifier = Modifier
                    .fillMaxWidth(),
                repeatSelected = state.repeatSelected,
                onRepeatTypeSelected = {
                    onAction(CreateTransactionAction.OnRepeatSelected(it))
                }
            )
        }
    }
}

@Composable
private fun DropDownCategories(
    modifier: Modifier = Modifier,
    categorySelected: Category,
    onCategorySelected: (Category) -> Unit
) {
    var menuMaxWidth by remember {
        mutableIntStateOf(0)
    }
    val menuMaxWidthDp = with(LocalDensity.current) { menuMaxWidth.toDp() }

    var menuIsExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .onSizeChanged { size ->
                menuMaxWidth = maxOf(menuMaxWidth, size.width)
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                menuIsExpanded = true
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .size(40.dp)
                        .background(
                            color = PrimaryFixed
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = categorySelected.icon
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = categorySelected.title,
                    modifier = Modifier
                        .weight(1f),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
        DropdownMenu(
            expanded = menuIsExpanded,
            onDismissRequest = { menuIsExpanded = false },
            containerColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(menuMaxWidthDp),
            offset = DpOffset(0.dp, (-10).dp)
        ) {
            Category.entries.fastForEach { category ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .size(40.dp)
                                    .background(
                                        color = PrimaryFixed
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = category.icon
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = category.title,
                                modifier = Modifier
                                    .weight(1f),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    },
                    onClick = {
                        menuIsExpanded = false
                        onCategorySelected(category)
                    }
                )
            }
        }
    }
}

@Composable
private fun DropDownRepeat(
    modifier: Modifier = Modifier,
    repeatSelected: Repeat,
    onRepeatTypeSelected: (Repeat) -> Unit
) {
    var menuMaxWidth by remember {
        mutableIntStateOf(0)
    }
    val menuMaxWidthDp = with(LocalDensity.current) { menuMaxWidth.toDp() }

    var menuIsExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .onSizeChanged { size ->
                menuMaxWidth = maxOf(menuMaxWidth, size.width)
            }
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                menuIsExpanded = true
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .size(40.dp)
                        .background(
                            color = PrimaryFixed
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\uD83D\uDD04"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = repeatSelected.title,
                    modifier = Modifier
                        .weight(1f),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
        DropdownMenu(
            expanded = menuIsExpanded,
            onDismissRequest = { menuIsExpanded = false },
            containerColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(menuMaxWidthDp)
        ) {
            Repeat.entries.fastForEach { type ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.weight(0.1f))
                            Text(
                                text = type.title,
                                modifier = Modifier
                                    .weight(1f),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    },
                    onClick = {
                        menuIsExpanded = false
                        onRepeatTypeSelected(type)
                    }
                )
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
