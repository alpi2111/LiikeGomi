package com.liike.liikegomi.administrate_products.adapters

import com.liike.liikegomi.background.firebase_db.entities.Productos

interface AdminProductsCallback {
    fun update(product: Productos)
    fun delete(product: Productos, position: Int)
}