package com.liike.liikegomi.background.firebase_db.entities


import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

class Carrito(
    @get:PropertyName("nombre_usuario")
    @set:PropertyName("nombre_usuario")
    var nombreUsuario: String?,
    @get:PropertyName("productos")
    @set:PropertyName("productos")
    var productos: List<Producto>?,
    @get:Exclude
    @set:Exclude
    var idFirebaseCarrito: String?
): Serializable {
    class Producto(
        @get:PropertyName("nombre")
        @set:PropertyName("nombre")
        var nombre: String?,
        @get:PropertyName("cantidad")
        @set:PropertyName("cantidad")
        var cantidad: Int?,
        @get:PropertyName("id_producto")
        @set:PropertyName("id_producto")
        var idProducto: Int?,
        @get:PropertyName("precio_unidad")
        @set:PropertyName("precio_unidad")
        var precioUnidad: Double?,
//        @get:PropertyName("imagen")
//        @set:PropertyName("imagen")
//        var imagen: Blob?,
    ): Serializable {
        constructor() : this(null, null, null, null)
    }

    constructor() : this(null, null, null)
}