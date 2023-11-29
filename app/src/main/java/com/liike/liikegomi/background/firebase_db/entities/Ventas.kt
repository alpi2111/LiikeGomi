package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class Ventas(
    @PropertyName("id_usuario") val idUsuario: Int,
    @PropertyName("fecha_compra") val fechaCompra: Long,
    @PropertyName("total_productos") val totalProductos: Int,
    @PropertyName("total_compra") val totalCompra: Double,
    @PropertyName("id_venta") val idVenta: Int = 0,
): FirebaseObjectInterface
