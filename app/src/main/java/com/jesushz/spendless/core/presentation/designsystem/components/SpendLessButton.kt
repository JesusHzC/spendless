package com.jesushz.spendless.core.presentation.designsystem.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SpendLessButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onButtonClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onButtonClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        )
    ) {
        content()
    }
}
