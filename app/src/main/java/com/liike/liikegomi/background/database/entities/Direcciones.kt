package com.liike.liikegomi.background.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Direcciones(
    @ColumnInfo(name = "direccion") val direccion: String,
    @ColumnInfo(name = "estado") val estado: String,
    @ColumnInfo(name = "municipio") val municipio: String,
    @ColumnInfo(name = "colonia") val colonia: String,
    @ColumnInfo(name = "calle") val calle: String,
    @ColumnInfo(name = "cp") val cp: String,
    @ColumnInfo(name = "num_exterior") val numExterior: String,
    @ColumnInfo(name = "num_interior") val numInterior: String,
    @ColumnInfo(name = "telefono") val telefono: String,
    @ColumnInfo(name = "referencias") val referencias: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_direccion") val idDireccion: Int = 0,
)