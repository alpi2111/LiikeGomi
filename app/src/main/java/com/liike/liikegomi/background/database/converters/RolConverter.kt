package com.liike.liikegomi.background.database.converters

import android.content.res.Resources.NotFoundException
import androidx.room.TypeConverter
import com.liike.liikegomi.background.database.types.RolType

object RolConverter {
    @TypeConverter
    fun rolTypeToString(rolType: RolType): String {
        return rolType.value
    }

    @TypeConverter
    fun stringToRolType(string: String): RolType {
        return when (string) {
            RolType.USER.value -> RolType.USER
            RolType.ADMIN.value -> RolType.ADMIN
            else -> throw NotFoundException("The rol does not belong to the valid roles. See RolType class")
        }
    }
}