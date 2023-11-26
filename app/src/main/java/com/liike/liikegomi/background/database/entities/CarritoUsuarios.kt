package com.liike.liikegomi.background.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Productos::class,
            parentColumns = arrayOf("id_producto"),
            childColumns = arrayOf("id_productos"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false,
        )
    ]
)
data class CarritoUsuarios(
    @ColumnInfo("id_productos", index = true) val idProducto: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_sesion") val idSesion: Int = 0,
)
