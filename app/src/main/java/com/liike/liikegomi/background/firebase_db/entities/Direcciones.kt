package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

class Direcciones(
//    @get:PropertyName("direccion")
//    @set:PropertyName("direccion")
//    var direccion: String,
    @get:PropertyName("estado")
    @set:PropertyName("estado")
    var estado: String,
    @get:PropertyName("municipio")
    @set:PropertyName("municipio")
    var municipio: String,
    @get:PropertyName("colonia")
    @set:PropertyName("colonia")
    var colonia: String,
    @get:PropertyName("calle")
    @set:PropertyName("calle")
    var calle: String,
    @get:PropertyName("cp")
    @set:PropertyName("cp")
    var cp: String,
    @get:PropertyName("num_exterior")
    @set:PropertyName("num_exterior")
    var numExterior: String,
    @get:PropertyName("num_interior")
    @set:PropertyName("num_interior")
    var numInterior: String,
    @get:PropertyName("telefono")
    @set:PropertyName("telefono")
    var telefono: String,
    @get:PropertyName("referencias")
    @set:PropertyName("referencias")
    var referencias: String,
//    @get:PropertyName("id_direccion")
//    @set:PropertyName("id_direccion")
//    var idDireccion: Int = 0,
) : FirebaseObjectInterface() {
    override fun toString(): String = calle

    fun formattedAddress(): String {
        return "${calle.trim()} ${numExterior.trim()} ${numInterior.trim()} - ${colonia.trim()}; ${cp.trim()}, ${municipio.trim()}, ${estado.trim()}." +
                "\nTeléfono: ${telefono.trim()} - Referencias: ${referencias.trim()}"
    }
}