package com.liike.liikegomi.background.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.liike.liikegomi.background.database.types.CategoryType

@Entity
data class Categoria(
    @ColumnInfo("categoria") val categoria: CategoryType,
    @ColumnInfo("visible") val isVisible: Boolean,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_categoria") val idCategoria: Int = 0,
)
