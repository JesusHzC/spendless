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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jesushz.spendless.R
import com.jesushz.spendless.core.presentation.designsystem.theme.OnPrimaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.PrimaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.SpendLessTheme
import com.jesushz.spendless.core.presentation.designsystem.theme.SurfaceContainerLow
import com.jesushz.spendless.dashboard.domain.TransactionType
import com.jesushz.spendless.dashboard.presentation.create_transaction.components.CreateTransactionTextField
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
                    text = "Create Transaction",
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
                    .align(Alignment.CenterHorizontally)
            ) {
                CreateTransactionTextField(
                    value = "",
                    onValueChange = {},
                    hint = "Receiver",
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    imeAction = ImeAction.Next,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                CreateTransactionTextField(
                    value = "",
                    onValueChange = {},
                    hint = "00.00",
                    textStyle = MaterialTheme.typography.displayMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    keyBoardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    extraContent = {
                        Text(
                            text = "-$",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CreateTransactionTextField(
                    value = "",
                    onValueChange = {},
                    hint = "Add note",
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    imeAction = ImeAction.Done,
                    extraContent = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            DropDownCategories(
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            DropDownRepeat(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DropDownCategories(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(16.dp)
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

            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Other",
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun DropDownRepeat(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(16.dp)
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

            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Does not repeat",
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
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
