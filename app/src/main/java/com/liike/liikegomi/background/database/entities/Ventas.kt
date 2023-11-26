package com.liike.liikegomi.background.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Usuarios::class,
            parentColumns = arrayOf("id_usuario"),
            childColumns = arrayOf("id_usuario"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false,
        ),
    ]
)
data class Ventas(
    @ColumnInfo("id_usuario", index = true) val idUsuario: Int,
    @ColumnInfo("fecha_compra") val fechaCompra: Long,
    @ColumnInfo("total_productos") val totalProductos: Int,
    @ColumnInfo("total_compra") val totalCompra: Double,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_venta") val idVenta: Int = 0,
)
