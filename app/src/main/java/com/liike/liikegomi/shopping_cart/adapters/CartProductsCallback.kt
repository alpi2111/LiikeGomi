package com.liike.liikegomi.shopping_cart.adapters

import com.liike.liikegomi.background.firebase_db.entities.Carrito

interface CartProductsCallback {
    fun onUpdateCart(cartProduct: Carrito.Producto)
    fun onRemoveCart(cartProduct: Carrito.Producto)
    fun onDeleteCart(cartProduct: Carrito.Producto, position: Int)
}