package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class Direcciones(
    @PropertyName("direccion") val direccion: String,
    @PropertyName("estado") val estado: String,
    @PropertyName("municipio") val municipio: String,
    @PropertyName("colonia") val colonia: String,
    @PropertyName("calle") val calle: String,
    @PropertyName("cp") val cp: String,
    @PropertyName("num_exterior") val numExterior: String,
    @PropertyName("num_interior") val numInterior: String,
    @PropertyName("telefono") val telefono: String,
    @PropertyName("referencias") val referencias: String,
    @PropertyName("id_direccion") val idDireccion: Int = 0,
): FirebaseObjectInterface