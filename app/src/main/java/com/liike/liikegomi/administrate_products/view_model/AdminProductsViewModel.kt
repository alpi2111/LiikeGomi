package com.liike.liikegomi.administrate_products.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class AdminProductsViewModel: BaseViewModel() {

    private val _categoriesList: MutableLiveData<List<Categoria>> = MutableLiveData()
    val mCategoriesList: LiveData<List<Categoria>> = _categoriesList
    private val _productsList = MutableLiveData<List<Productos>>()
    val mProductsList: LiveData<List<Productos>> = _productsList
    private val mImageBytesList: MutableList<Pair<Int, ByteArray?>> = mutableListOf()
    private val _indexProductDeleted = MutableLiveData<Int>()
    val mIndexProductDeleted: LiveData<Int> = _indexProductDeleted

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

    fun notifyImageBytesWithIndex(imageBytes: ByteArray?, idProduct: Int) {
        try {
            val position = mImageBytesList.indexOfFirst { (id, _) -> id == idProduct }
            if (position != -1) {
                val item = mImageBytesList[position]
                mImageBytesList[position] = item.first to imageBytes
            } else {
                mImageBytesList.add(idProduct to imageBytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mImageBytesList.add(idProduct to imageBytes)
        }
    }

    fun getImageBytesByProductId(idProduct: Int): ByteArray? {
        return try {
            mImageBytesList.first { (id, _) -> id == idProduct }.second
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun deleteProduct(product: Productos) {
        viewModelScope.launch {
            progressMessage.value = "Eliminando producto"
            val wasDeleted = FirebaseUtils.deleteProduct(product)
            progressMessage.value = null
            if (wasDeleted)
                _indexProductDeleted.value = product.productId
            else
                toastMessage.value = "Ocurrió un error al eliminar el producto"
        }
    }

    fun updateProduct(product: Productos) {
        viewModelScope.launch {
            progressMessage.value = "Actualizando"
            val wasUpdated = FirebaseUtils.updateProduct(product)
            progressMessage.value = null
            if (wasUpdated)
                toastMessage.value = "Producto actualizado"
            else
                toastMessage.value = "Error al actualizar, intenta nuevamente"
        }
    }


}