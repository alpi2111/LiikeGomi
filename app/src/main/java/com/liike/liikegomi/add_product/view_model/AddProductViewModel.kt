package com.liike.liikegomi.add_product.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.base.viewmodel.BaseViewModel

class AddProductViewModel: BaseViewModel() {

    private val _categories: MutableLiveData<List<Categoria>> = MutableLiveData()
    val mCategories: LiveData<List<Categoria>> = _categories
    private val _productAdded: MutableLiveData<Boolean> = MutableLiveData()
    val mProductAdded: LiveData<Boolean> = _productAdded

    fun getCategories() {
        progressMessage.value = "Obteniendo datos"
        FirebaseUtils.getProductCategories { wasSuccess, categories, message ->
            if (!wasSuccess)
                toastMessage.value = message
            progressMessage.value = null
            _categories.value = categories

        }
    }

    fun getProductIdAndThenAdd(product: Productos) {
        progressMessage.value = "Agregando producto"
        FirebaseUtils.getLastProductCategoryId { wasSuccess, id, message ->
            if (wasSuccess) {
                addProduct(product.apply { idCategoria = id!! })
            } else {
                toastMessage.value = message
                progressMessage.value = null
                _productAdded.value = false
            }
        }
    }

    private fun addProduct(product: Productos) {
        FirebaseUtils.addProduct(product) { wasAdded, message ->
            progressMessage.value = null
            _productAdded.value = wasAdded
            toastMessage.value = message ?: "Producto agregado"
        }
    }
}