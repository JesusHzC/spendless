package com.jesushz.spendless.core.domain.preferences

import com.jesushz.spendless.core.domain.security.Biometrics
import com.jesushz.spendless.core.domain.security.LockedOutDuration
import com.jesushz.spendless.core.domain.security.SessionDuration

data class SecurityPreferences(
    val biometrics: Biometrics = Biometrics.DISABLE,
    val sessionDuration: SessionDuration = SessionDuration.FIVE_MINUTES,
    val lockedOutDuration: LockedOutDuration = LockedOutDuration.FIFTEEN_SECONDS,
)
