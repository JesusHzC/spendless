package com.jesushz.spendless.core.database.converters

import androidx.room.TypeConverter
import com.jesushz.spendless.core.domain.transactions.Category

class CategoryTypeConverter {

    @TypeConverter
    fun fromCategory(value: Category): String {
        return value.name
    }

    @TypeConverter
    fun toCategory(value: String): Category {
        return Category.valueOf(value)
    }

}
