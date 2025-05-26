package com.jesushz.spendless.core.presentation.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesushz.spendless.R
import com.jesushz.spendless.core.domain.transactions.Transaction
import com.jesushz.spendless.core.domain.transactions.TransactionType
import com.jesushz.spendless.core.presentation.designsystem.theme.PrimaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.SecondaryFixed
import com.jesushz.spendless.core.presentation.designsystem.theme.Success

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    itemSelected: Transaction?,
    amountFormatted: String,
    onItemSelected: () -> Unit,
    onItemEdit: () -> Unit,
    onItemDelete: () -> Unit
) {
    var isRevealed by remember { mutableStateOf(false) }

    SwipeableItemWithActions(
        isRevealed = isRevealed,
        onExpanded = {
            isRevealed = true
        },
        onCollapsed = {
            isRevealed = false
        },
        actions = {
            SwipeableAction(
                icon = Icons.Default.Edit,
                backgroundColor = Success,
                contentDescription = stringResource(R.string.edit_transaction_action),
                onActionClick = {
                    onItemEdit()
                    isRevealed = false
                }
            )
            SwipeableAction(
                icon = Icons.Default.Delete,
                backgroundColor = MaterialTheme.colorScheme.error,
                contentDescription = stringResource(R.string.delete_transaction_action),
                onActionClick = {
                    onItemDelete()
                    isRevealed = false
                }
            )
        }
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = if (transaction == itemSelected && transaction.note.isNotEmpty()) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    Color.Transparent
                },
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (transaction == itemSelected && transaction.note.isNotEmpty()) {
                    4.dp
                } else {
                    0.dp
                }
            ),
            onClick = {
                onItemSelected()
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .size(56.dp)
                            .background(
                                color = if (transaction.transactionType == TransactionType.INCOME) {
                                    SecondaryFixed.copy(alpha = 0.4f)
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
                            fontSize = 20.sp
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
                    Text(
                        text = if (transaction.transactionType == TransactionType.INCOME) {
                            "$${transaction.amount}"
                        } else {
                            amountFormatted
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = if (transaction.transactionType == TransactionType.INCOME) {
                            Success
                        } else {
                            MaterialTheme.typography.titleLarge.color
                        }
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                AnimatedVisibility(
                    visible = transaction == itemSelected && transaction.note.isNotEmpty()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.weight(0.2f))
                        Text(
                            text = transaction.note,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}