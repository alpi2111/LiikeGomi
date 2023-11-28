package com.liike.liikegomi.background.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["correo", "nombre_usuario"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Rol::class,
            parentColumns = arrayOf("id_rol"),
            childColumns = arrayOf("id_rol"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false,
        ),
    ],
)
data class Usuarios(
    @ColumnInfo("nombre") val name: String,
    @ColumnInfo("apellido") val lastName: String,
    @ColumnInfo("contra") val password: String,
    @ColumnInfo("correo") val email: String,
    @ColumnInfo("nombre_usuario") val userName: String,
    @ColumnInfo("fecha_nacimiento") val birthDay: Long,
    @ColumnInfo("activo") val isActive: Boolean,
    @ColumnInfo("id_rol", index = true) var idRol: Int = -1,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_usuario") val idUser: Int = 0,
)
