package com.jesushz.spendless.auth.presentation.pin

import com.jesushz.spendless.auth.presentation.pin.components.Keys
import com.jesushz.spendless.core.presentation.ui.UiText

sealed interface PinAction {

    data class OnKeyPressed(val key: Keys): PinAction
    data object OnBackPressed: PinAction
    data object OnBiometricsSuccess : PinAction
    data class OnBiometricsError(val error: UiText) : PinAction

}
