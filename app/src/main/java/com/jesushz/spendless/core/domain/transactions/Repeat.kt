package com.jesushz.spendless.core.domain.transactions

import com.jesushz.spendless.core.util.getCurrentDayOfMonthWithSuffix
import com.jesushz.spendless.core.util.getCurrentMonthName
import com.jesushz.spendless.core.util.getCurrentWeekName

enum class Repeat(val title: String) {
    NOT_REPEAT("Does not repeat"),
    DAILY("Daily"),
    WEEKLY("Weekly on ${getCurrentWeekName()}"),
    MONTHLY("Monthly on the ${getCurrentDayOfMonthWithSuffix()}"),
    YEARLY("Yearly on ${getCurrentMonthName()} ${getCurrentDayOfMonthWithSuffix()}")
}
