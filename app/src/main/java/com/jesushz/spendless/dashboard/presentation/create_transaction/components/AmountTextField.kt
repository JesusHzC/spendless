package com.jesushz.spendless.dashboard.presentation.create_transaction.components

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jesushz.spendless.MainActivity
import com.jesushz.spendless.R
import com.jesushz.spendless.core.presentation.designsystem.theme.Success
import com.jesushz.spendless.core.domain.transactions.ExpenseFormat
import com.jesushz.spendless.core.domain.transactions.TransactionType
import com.jesushz.spendless.core.util.isNumber

@Composable
fun AmountTextField(
    modifier: Modifier = Modifier,
    amount: String,
    hint: String = stringResource(id = R.string.amount_hint),
    onAmountChange: (String) -> Unit,
    expenseFormat: ExpenseFormat = ExpenseFormat.POSITIVE,
    transactionType: TransactionType = TransactionType.EXPENSE
) {
    val textStyle = MaterialTheme.typography.displayMedium
    var isFocused by remember { mutableStateOf(false) }
    val activity = LocalActivity.current as MainActivity

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (expenseFormat) {
            ExpenseFormat.NEGATIVE -> {
                Text(
                    text = if (transactionType == TransactionType.EXPENSE) {
                        "-$"
                    } else {
                        "$"
                    },
                    style = textStyle,
                    color = if (transactionType == TransactionType.EXPENSE) {
                        MaterialTheme.colorScheme.error
                    } else {
                        Success
                    }
                )
            }
            ExpenseFormat.POSITIVE -> {
                Text(
                    text = if (transactionType == TransactionType.EXPENSE) {
                        "-($"
                    } else {
                        "($"
                    },
                    style = textStyle,
                    color = if (transactionType == TransactionType.EXPENSE) {
                        MaterialTheme.colorScheme.error
                    } else {
                        Success
                    }
                )
            }
        }
        BasicTextField(
            value = amount,
            onValueChange = {
                activity.onUserInteraction()
                if (it.isNumber() || it.isEmpty()) {
                    onAmountChange(it)
                }
            },
            singleLine = true,
            textStyle = textStyle.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .width(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (amount.isEmpty() && !isFocused) {
                        Text(
                            text = hint,
                            style = textStyle,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                    innerTextField()
                }
            }
        )
        if (expenseFormat == ExpenseFormat.POSITIVE) {
            Text(
                text = ")",
                style = textStyle,
                color = if (transactionType == TransactionType.EXPENSE) {
                    MaterialTheme.colorScheme.error
                } else {
                    Success
                }
            )
        }
    }
}