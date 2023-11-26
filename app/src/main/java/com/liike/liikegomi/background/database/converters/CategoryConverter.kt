package com.liike.liikegomi.background.database.converters

import android.content.res.Resources.NotFoundException
import androidx.room.TypeConverter
import com.liike.liikegomi.background.database.types.CategoryType

object CategoryConverter {
    @TypeConverter
    fun rolTypeToString(categoryType: CategoryType): String {
        return categoryType.value
    }

    @TypeConverter
    fun stringToRolType(string: String): CategoryType {
        return when (string) {
            CategoryType.NATURAL.value -> CategoryType.NATURAL
            CategoryType.ENCHILADO.value -> CategoryType.ENCHILADO
            CategoryType.VITAMINAS.value -> CategoryType.VITAMINAS
            else -> throw NotFoundException("The category does not belong to the valid roles. See CategoryType class")
        }
    }
}