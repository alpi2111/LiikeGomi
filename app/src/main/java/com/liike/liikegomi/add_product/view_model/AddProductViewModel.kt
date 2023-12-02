package com.liike.liikegomi.add_product.view_model

import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.base.viewmodel.BaseViewModel

class AddProductViewModel: BaseViewModel() {
    fun addProduct(product: Productos) {
        FirebaseUtils.addProduct(product) { wasAdded, message ->
            toastMessage.value = message ?: "Producto agregado"
        }
    }
}