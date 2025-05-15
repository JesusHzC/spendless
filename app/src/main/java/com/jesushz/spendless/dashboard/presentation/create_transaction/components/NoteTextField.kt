package com.jesushz.spendless.dashboard.presentation.create_transaction.components

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.jesushz.spendless.R

@Composable
fun NoteTextField(
    modifier: Modifier = Modifier,
    note: String,
    onNoteChange: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = note,
        onValueChange = onNoteChange,
        textStyle = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = false,
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        decorationBox = { innerTextField ->
            if (note.isEmpty() && !isFocused) {
                Text(
                    text = stringResource(R.string.note_hint),
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