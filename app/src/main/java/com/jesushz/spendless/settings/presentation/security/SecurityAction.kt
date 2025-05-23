package com.jesushz.spendless.settings.presentation.security

import com.jesushz.spendless.core.domain.security.Biometrics
import com.jesushz.spendless.core.domain.security.LockedOutDuration
import com.jesushz.spendless.core.domain.security.SessionDuration

sealed interface SecurityAction {

    data class OnBiometricsSelected(val biometrics: Biometrics): SecurityAction
    data class OnSessionDurationSelected(val sessionDuration: SessionDuration): SecurityAction
    data class OnLockedOutDurationSelected(val lockedOutDuration: LockedOutDuration): SecurityAction
    data object OnSaveClick: SecurityAction
    data object OnBackClick: SecurityAction

}
