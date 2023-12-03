package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

class CarritoUsuarios(
    @get:PropertyName("id_productos")
    val idProduct: Int,
    @get:PropertyName("id_sesion")
    val idSession: Int = 0,
): FirebaseObjectInterface() {
    override fun toString(): String = idProduct.toString()
}
