package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.database.types.RolType
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

class Usuarios(
    @get:PropertyName("nombre")
    @set:PropertyName("nombre")
    var name: String,
    @get:PropertyName("apellido")
    @set:PropertyName("apellido")
    var lastName: String,
    @get:PropertyName("contra")
    @set:PropertyName("contra")
    var password: String,
    @get:PropertyName("correo")
    @set:PropertyName("correo")
    var email: String,
    @get:PropertyName("nombre_usuario")
    @set:PropertyName("nombre_usuario")
    var userName: String,
    @get:PropertyName("fecha_nacimiento")
    @set:PropertyName("fecha_nacimiento")
    var birthDay: Timestamp,
    @get:PropertyName("numero_telefonico")
    @set:PropertyName("numero_telefonico")
    var phoneNumber: String,
    @get:PropertyName("activo")
    @set:PropertyName("activo")
    var isActive: Boolean = true,
    @get:PropertyName("direcciones")
    @set:PropertyName("direcciones")
    var address: List<Direcciones>? = null,
    @get:PropertyName("esta_logueado")
    @set:PropertyName("esta_logueado")
    var isLogged: Boolean = false,
    @get:Exclude
    @set:Exclude
    var rolType: RolType = RolType.USER
): FirebaseObjectInterface() {
    @get:PropertyName("rol")
    @set:PropertyName("rol")
    var rol: Map<String, String> = mapOf("type" to rolType.value)
    constructor() : this("", "", "", "", "", Timestamp.now(), "", false, null, false)

    override fun toString(): String = name
}
