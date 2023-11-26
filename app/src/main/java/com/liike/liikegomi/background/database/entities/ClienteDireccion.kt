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
        ForeignKey(
            entity = Direcciones::class,
            parentColumns = arrayOf("id_direccion"),
            childColumns = arrayOf("id_direccion"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false,
        )
    ]
)
data class ClienteDireccion(
    @ColumnInfo("id_usuario", index = true) val idUsuario: Int,
    @ColumnInfo("id_direccion", index = true) val idDireccion: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_clientedireccion") val idClienteDireccion: Int = 0
)
