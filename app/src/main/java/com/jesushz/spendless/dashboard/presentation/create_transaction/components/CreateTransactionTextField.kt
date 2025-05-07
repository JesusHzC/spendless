package com.jesushz.spendless.dashboard.presentation.create_transaction.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CreateTransactionTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = "",
    textStyle: TextStyle = TextStyle.Default,
    keyBoardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    extraContent: (@Composable () -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        textStyle = textStyle,
        cursorBrush = SolidColor(textStyle.color),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyBoardType,
            imeAction = imeAction
        ),
        decorationBox = { innerBox ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                extraContent?.let {
                    it()
                    Spacer(modifier = Modifier.width(4.dp))
                }
                if (value.isEmpty() && !isFocused) {
                    Text(
                        text = hint,
                        style = textStyle.copy(
                            color = textStyle.color.copy(alpha = 0.5f)
                        )
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .widthIn(
                                max = 30.dp
                            )
                    ) {
                        innerBox()
                    }
                }
            }
        }
    )

}
