package com.jesushz.spendless.auth.presentation.pin

import com.jesushz.spendless.auth.presentation.pin.components.Keys

sealed interface PinAction {

    data class OnKeyPressed(val key: Keys): PinAction
    data object OnBackPressed: PinAction

}
