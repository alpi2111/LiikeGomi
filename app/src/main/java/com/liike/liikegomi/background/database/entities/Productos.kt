package com.liike.liikegomi.background.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Categoria::class,
            parentColumns = arrayOf("id_categoria"),
            childColumns = arrayOf("id_cat"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false
        )
    ]
)
class Productos(
    @ColumnInfo("nombre_producto") val productName: String,
    @ColumnInfo("descripcion") val productDescription: String,
    @ColumnInfo("precio_producto") val productPrice: Double,
    @ColumnInfo("stocks") val productStock: Int,
    @ColumnInfo("id_cat", index = true) var idCategoria: Int,
    @ColumnInfo("visible") var isVisible: Boolean = true,
    @ColumnInfo("imagen", typeAffinity = ColumnInfo.BLOB) var productImage: ByteArray? = null,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_producto") val idProduct: Int = 0,
)
