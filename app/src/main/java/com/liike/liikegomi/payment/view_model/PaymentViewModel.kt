package com.liike.liikegomi.payment.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Direcciones
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class PaymentViewModel: BaseViewModel() {

    private val _userAddresses = MutableLiveData<List<Direcciones>>()
    val mUserAddresses: LiveData<List<Direcciones>> = _userAddresses

    fun getUserAddresses() {
        viewModelScope.launch {
            progressMessage.value = "Cargando direcciones"
            val directions = FirebaseUtils.getUserAddress()
            if (directions.isEmpty()) {
                toastMessage.value = "No tienes direcciones registradas"
            }
            _userAddresses.value = directions
            progressMessage.value = null
        }
    }
}