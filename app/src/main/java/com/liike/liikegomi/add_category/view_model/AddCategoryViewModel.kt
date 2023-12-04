package com.liike.liikegomi.add_category.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.base.viewmodel.BaseViewModel

class AddCategoryViewModel: BaseViewModel() {

    private val _categoryAdded: MutableLiveData<Boolean> = MutableLiveData()
    val mCategoryAdded: LiveData<Boolean> = _categoryAdded

    fun getCategoryIdAndThenAdd(categoria: Categoria) {
        progressMessage.value = "Agregando categoría"
        FirebaseUtils.getLastCategoryId { wasSuccess, id, message ->
            if (wasSuccess) {
                addCategory(categoria.apply { idCategory = id!! })
            } else {
                toastMessage.value = message
                progressMessage.value = null
                _categoryAdded.value = false
            }
        }
    }

    private fun addCategory(category: Categoria) {
        FirebaseUtils.addCategory(category) { wasAdded, message ->
            progressMessage.value = null
            _categoryAdded.value = wasAdded
            toastMessage.value = message ?: "Categoría agregada"
        }
    }
}