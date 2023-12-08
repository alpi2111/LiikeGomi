package com.liike.liikegomi.add_address.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Direcciones
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class AddAddressViewModel: BaseViewModel() {

    private val _addressAdded = MutableLiveData<Direcciones?>()
    val mAddressAdded: LiveData<Direcciones?> = _addressAdded

    fun addAddress(address: Direcciones) {
        viewModelScope.launch {
            progressMessage.value = "Guardando dirección"
            val wasAdded = FirebaseUtils.addAddress(address)
            if (!wasAdded) {
                toastMessage.value = "No se pudo agregar la dirección, intenta nuevamente"
                _addressAdded.value = null
            } else {
                _addressAdded.value = address
            }
            progressMessage.value
        }
    }
}