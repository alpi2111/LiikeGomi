package com.liike.liikegomi.administrate_category.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdminCategoryViewModel : BaseViewModel() {

    private val _categories = MutableLiveData<List<Categoria>>()
    val mCategories: LiveData<List<Categoria>> = _categories
    private val _deletedCategoryPosition = MutableLiveData<Int>()
    val mDeletedCategoryPosition: LiveData<Int> = _deletedCategoryPosition

    fun getCategories() {
        progressMessage.value = "Cargando categorías"
        FirebaseUtils.getProductCategories { wasSuccess, categorias, message ->
            if (wasSuccess)
                _categories.value = categorias!!
            else
                toastMessage.value = message
            progressMessage.value = null
        }
    }

    fun updateCategory(categoria: Categoria) {
        progressMessage.value = "Actualizando"
        FirebaseUtils.updateCategory(categoria) { _, message ->
            toastMessage.value = message ?: "Categoría ${categoria.category} actualizada"
            progressMessage.value = null
        }
    }

    fun deleteCategory(idCategory: String, position: Int) {
        progressMessage.value = "Eliminando"
        FirebaseUtils.deleteCategory(idCategory) { wasSuccess, message ->
            if (wasSuccess) {
                _deletedCategoryPosition.value = position
            }
            viewModelScope.launch {
                _deletedCategoryPosition.value = position
                delay(500)
                toastMessage.value = message ?: "Categoría eliminada"
                progressMessage.value = null
            }
        }
    }
}