package com.liike.liikegomi.main.view_model

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.base.viewmodel.BaseViewModel

class MainViewModel: BaseViewModel() {

    private val _categoriesList: MutableLiveData<List<Categoria>> = MutableLiveData()
    val mCategoriesList: LiveData<List<Categoria>> = _categoriesList

    fun getCategories(activity: AppCompatActivity) {
        progressMessage.value = "Obteniendo productos"
        FirebaseUtils.getProductCategories(activity) { wasSuccess, categoriaList, message ->
            if (wasSuccess) {
                _categoriesList.value = categoriaList
            } else {
                toastMessage.value = message
            }
            progressMessage.value = null
        }
    }
}