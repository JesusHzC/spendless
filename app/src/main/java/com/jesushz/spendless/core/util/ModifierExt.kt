package com.jesushz.spendless.core.util

import androidx.activity.compose.LocalActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import com.jesushz.spendless.MainActivity


fun Modifier.userInteraction() = composed {
    val activity = LocalActivity.current as MainActivity
    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                awaitPointerEvent()
                activity.onUserInteraction()
            }
        }
    }
}
