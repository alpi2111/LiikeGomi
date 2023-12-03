package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

class ClienteDireccion(
    @get:PropertyName("id_usuario")
    val idUser: Int,
    @get:PropertyName("id_direccion")
    val idAddress: Int,
    @get:PropertyName("id_clientedireccion")
    val idClientAddress: Int = 0
): FirebaseObjectInterface() {
    override fun toString(): String = idClientAddress.toString()
}
