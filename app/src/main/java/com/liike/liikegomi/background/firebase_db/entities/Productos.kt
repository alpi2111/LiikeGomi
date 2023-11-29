package com.liike.liikegomi.background.firebase_db.entities

import com.google.firebase.firestore.PropertyName
import com.liike.liikegomi.background.firebase_db.base.FirebaseObjectInterface

class Productos(
    @PropertyName("nombre_producto") val productName: String,
    @PropertyName("descripcion") val productDescription: String,
    @PropertyName("precio_producto") val productPrice: Double,
    @PropertyName("stocks") val productStock: Int,
    @PropertyName("id_cat") var idCategoria: Int,
    @PropertyName("visible") var isVisible: Boolean = true,
    @PropertyName("imagen") var productImage: ByteArray? = null,
    @PropertyName("id_producto") val idProduct: Int = 0,
): FirebaseObjectInterface
