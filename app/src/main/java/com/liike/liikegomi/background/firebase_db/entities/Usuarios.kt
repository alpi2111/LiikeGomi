package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class Usuarios(
    @PropertyName("nombre") val name: String,
    @PropertyName("apellido") val lastName: String,
    @PropertyName("contra") val password: String,
    @PropertyName("correo") val email: String,
    @PropertyName("nombre_usuario") val userName: String,
    @PropertyName("fecha_nacimiento") val birthDay: Long,
    @PropertyName("activo") val isActive: Boolean = true,
    @PropertyName("direcciones") val address: List<Direcciones>? = null,
    @PropertyName("esta_logueado") val isLogged: Boolean = false
): FirebaseObjectInterface
