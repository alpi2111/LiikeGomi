package com.liike.liikegomi.main.ui.adapters

import com.liike.liikegomi.background.firebase_db.entities.Productos

interface AddCartItemCallback {
    fun addToCart(product: Productos)
}