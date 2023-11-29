package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

data class VentasDetalle(
    @get:PropertyName("id_productos") val idProducto: Int,
    @get:PropertyName("id_ventas") val idVenta: Int,
    @get:PropertyName("cantidad") val cantidad: Int,
    @get:PropertyName("subtotal") val subtotal: Double,
    @get:PropertyName("descuento") val descuento: Double,
    @get:PropertyName("id_ventadetalle") val idVentaDetalle: Int = 0,
): FirebaseObjectInterface
