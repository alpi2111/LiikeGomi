package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

class Productos(
    @get:PropertyName("nombre_producto") val productName: String,
    @get:PropertyName("descripcion") val productDescription: String,
    @get:PropertyName("precio_producto") val productPrice: Double,
    @get:PropertyName("stocks") val productStock: Int,
    @get:PropertyName("id_cat") var idCategoria: Int,
    @get:PropertyName("visible") var isVisible: Boolean = true,
    @get:PropertyName("imagen") var productImage: ByteArray? = null,
    @get:PropertyName("id_producto") val idProduct: Int = 0,
): FirebaseObjectInterface
