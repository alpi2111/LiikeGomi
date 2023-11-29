package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class VentasDetalle(
    @PropertyName("id_productos") val idProducto: Int,
    @PropertyName("id_ventas") val idVenta: Int,
    @PropertyName("cantidad") val cantidad: Int,
    @PropertyName("subtotal") val subtotal: Double,
    @PropertyName("descuento") val descuento: Double,
    @PropertyName("id_ventadetalle") val idVentaDetalle: Int = 0,
): FirebaseObjectInterface
