package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class Ventas(
    @get:PropertyName("id_usuario") val idUsuario: Int,
    @get:PropertyName("fecha_compra") val fechaCompra: Long,
    @get:PropertyName("total_productos") val totalProductos: Int,
    @get:PropertyName("total_compra") val totalCompra: Double,
    @get:PropertyName("id_venta") val idVenta: Int = 0,
): FirebaseObjectInterface
