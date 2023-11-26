package com.liike.liikegomi.background.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.liike.liikegomi.background.database.types.RolType

@Entity
data class Rol(
    @ColumnInfo("tipo") val tipo: RolType,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_rol") val idRol: Int = 0,
)
