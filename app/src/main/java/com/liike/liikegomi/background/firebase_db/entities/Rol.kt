package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.database.types.RolType
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class Rol(
    @PropertyName("tipo")
    val type: RolType,
    @PropertyName("id_rol")
    val idRol: Int = 0,
): FirebaseObjectInterface
