package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class Direcciones(
    @get:PropertyName("direccion") val direccion: String,
    @get:PropertyName("estado") val estado: String,
    @get:PropertyName("municipio") val municipio: String,
    @get:PropertyName("colonia") val colonia: String,
    @get:PropertyName("calle") val calle: String,
    @get:PropertyName("cp") val cp: String,
    @get:PropertyName("num_exterior") val numExterior: String,
    @get:PropertyName("num_interior") val numInterior: String,
    @get:PropertyName("telefono") val telefono: String,
    @get:PropertyName("referencias") val referencias: String,
    @get:PropertyName("id_direccion") val idDireccion: Int = 0,
): FirebaseObjectInterface