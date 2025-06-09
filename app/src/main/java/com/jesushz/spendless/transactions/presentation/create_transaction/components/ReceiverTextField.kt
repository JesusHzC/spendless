package com.jesushz.spendless.transactions.presentation.create_transaction.components

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.jesushz.spendless.MainActivity
import com.jesushz.spendless.R

@Composable
fun ReceiverTextField(
    modifier: Modifier = Modifier,
    receiver: String,
    onReceiverChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val activity = LocalActivity.current as MainActivity

    BasicTextField(
        value = receiver,
        onValueChange = {
            activity.onUserInteraction()
            onReceiverChange(it)
        },
        textStyle = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = true,
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        decorationBox = { innerTextField ->
            if (receiver.isEmpty() && !isFocused) {
                Text(
                    text = stringResource(R.string.receiver),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            innerTextField()
        }
    )
}
