package com.liike.liikegomi.administrate_products.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.base.viewmodel.BaseViewModel

class AdminProductsViewModel: BaseViewModel() {

    private val _categoriesList: MutableLiveData<List<Categoria>> = MutableLiveData()
    val mCategoriesList: LiveData<List<Categoria>> = _categoriesList
    private val _productsList = MutableLiveData<List<Productos>>()
    val mProductsList: LiveData<List<Productos>> = _productsList

    fun getCategories() {
        progressMessage.value = "Obteniendo productos"
        FirebaseUtils.getProductCategoriesListener { wasSuccess, categoriaList, message ->
            if (wasSuccess) {
                _categoriesList.value = categoriaList
            } else {
                toastMessage.value = message
            }
            progressMessage.value = null
        }
    }


    fun getProductsByCategory(idCategory: Int) {
        FirebaseUtils.getProductsByCategory(idCategory, ignoreVisibility = true) { wasSuccess, products, message ->
            if (wasSuccess)
                _productsList.value = products!!
            else
                toastMessage.value = message
        }
    }


}