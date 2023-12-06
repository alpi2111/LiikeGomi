package com.liike.liikegomi.main.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel: BaseViewModel() {

    private val _categoriesList: MutableLiveData<List<Categoria>> = MutableLiveData()
    val mCategoriesList: LiveData<List<Categoria>> = _categoriesList

    private val _productsList: MutableLiveData<List<Productos>> = MutableLiveData()
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
        FirebaseUtils.getProductsByCategory(idCategory, ignoreVisibility = false) { wasSuccess, products, message ->
            if (wasSuccess)
                _productsList.value = products!!
            else
                toastMessage.value = message
        }
    }

    fun addToCart(producto: Productos) {
        viewModelScope.launch {
            progressMessage.value = "Añadiendo"
            val firebaseUserId = SharedPrefs.string(SharedPreferenceKeys.USER_ID)
            if (firebaseUserId == null) {
                toastMessage.value = "El ID del usuario no es válido, reinicia tu sesión"
            } else {
                val wasAdded = FirebaseUtils.addProductToCart(firebaseUserId, producto, 1)
                if (!wasAdded) {
                    toastMessage.value = "El producto no pudo ser agregado al carrito"
                }
            }
            progressMessage.value = null
        }
    }
}