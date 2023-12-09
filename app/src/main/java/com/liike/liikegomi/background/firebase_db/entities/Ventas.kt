package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface
import com.liike.liikegomi.background.firebase_db.base.PaymentType

class Ventas(
    @get:PropertyName("id_usuario")
    @set:PropertyName("id_usuario")
    var idUsuario: String,
    @get:PropertyName("fecha_compra")
    @set:PropertyName("fecha_compra")
    var fechaCompra: Timestamp,
    @get:PropertyName("total_productos")
    @set:PropertyName("total_productos")
    var totalProductos: Int,
    @get:PropertyName("total_compra")
    @set:PropertyName("total_compra")
    var totalCompra: Double,
    @get:PropertyName("direccion_envio")
    @set:PropertyName("direccion_envio")
    var direccionEnvio: String,
    @get:PropertyName("lista_productos")
    @set:PropertyName("lista_productos")
    var listaProductos: List<Carrito.Producto>,
    @get:Exclude
    @set:Exclude
    var paymentType: PaymentType
) : FirebaseObjectInterface() {
    @get:PropertyName("tipo_pago")
    @set:PropertyName("tipo_pago")
    var tipoPago: String = paymentType.value

    override fun toString(): String = "${
        listaProductos.map {
            it.nombre!!.plus("# ${it.cantidad}")
        }
    }"

    constructor() : this("", Timestamp.now(), 0, 0.0, "", listOf(), PaymentType.OTHER(""))
}
