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
        ),
        ForeignKey(
            entity = Ventas::class,
            parentColumns = arrayOf("id_venta"),
            childColumns = arrayOf("id_ventas"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false,
        )
    ]
)
data class VentasDetalle(
    @ColumnInfo("id_productos", index = true) val idProducto: Int,
    @ColumnInfo("id_ventas", index = true) val idVenta: Int,
    @ColumnInfo("cantidad") val cantidad: Int,
    @ColumnInfo("subtotal") val subtotal: Double,
    @ColumnInfo("descuento") val descuento: Double,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_ventadetalle") val idVentaDetalle: Int = 0,
)
