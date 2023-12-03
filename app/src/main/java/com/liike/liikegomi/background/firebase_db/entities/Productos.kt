package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

class Productos(
    @get:PropertyName("nombre_producto")
    @set:PropertyName("nombre_producto")
    var productName: String,
    @get:PropertyName("descripcion")
    @set:PropertyName("descripcion")
    var productDescription: String,
    @get:PropertyName("precio_producto")
    @set:PropertyName("precio_producto")
    var productPrice: Double,
    @get:PropertyName("stocks")
    @set:PropertyName("stocks")
    var productStock: Int,
    @get:PropertyName("id_cat")
    @set:PropertyName("id_cat")
    var idCategoria: Int,
    @get:PropertyName("id_producto")
    @set:PropertyName("id_producto")
    var productId: Int = -1,
    @get:PropertyName("visible")
    @set:PropertyName("visible")
    var isVisible: Boolean = true,
    @get:PropertyName("imagen")
    @set:PropertyName("imagen")
    var productImage: Blob? = null,
): FirebaseObjectInterface() {
    constructor(): this("", "", 0.0, 0, 0)

    override fun toString(): String = productName
}
