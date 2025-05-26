package com.jesushz.spendless.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SwipeableAction(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    icon: ImageVector,
    contentDescription: String? = null,
    tint: Color = Color.White,
    onActionClick: () -> Unit
) {
    IconButton(
        onClick = onActionClick,
        modifier = modifier
            .background(backgroundColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}
