package com.liike.liikegomi.administrate_category.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.base.viewmodel.BaseViewModel

class AdminCategoryViewModel : BaseViewModel() {

    private val _categories = MutableLiveData<List<Categoria>>()
    val mCategories: LiveData<List<Categoria>> = _categories

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
}