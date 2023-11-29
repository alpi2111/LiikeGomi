package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class ClienteDireccion(
    @PropertyName("id_usuario")
    val idUser: Int,
    @PropertyName("id_direccion")
    val idAddress: Int,
    @PropertyName("id_clientedireccion")
    val idClientAddress: Int = 0
): FirebaseObjectInterface
