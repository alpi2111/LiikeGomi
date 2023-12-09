package com.liike.liikegomi.payment.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.base.PaymentType
import com.liike.liikegomi.background.firebase_db.entities.Carrito
import com.liike.liikegomi.background.firebase_db.entities.Direcciones
import com.liike.liikegomi.background.firebase_db.entities.Ventas
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class PaymentViewModel: BaseViewModel() {

    private val _userAddresses = MutableLiveData<List<Direcciones>>()
    val mUserAddresses: LiveData<List<Direcciones>> = _userAddresses
    private val _sellWasSuccess = MutableLiveData<Boolean>()
    val mSellWasSuccess: LiveData<Boolean> = _sellWasSuccess

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

    fun createSell(cartId: String, productsList: List<Carrito.Producto>, address: String, paymentType: PaymentType) {
        viewModelScope.launch {
            progressMessage.value = "Realizando compra"
            val idUser = SharedPrefs.string(SharedPreferenceKeys.USER_ID)!!
            var totalProducts = 0
            var totalAmount = 0.0
            productsList.forEach {
                totalProducts += it.cantidad!!
                totalAmount += it.cantidad!! * it.precioUnidad!!
            }
            val request = Ventas(
                idUsuario = idUser,
                fechaCompra = Timestamp.now(),
                totalProductos = totalProducts,
                totalCompra = totalAmount,
                direccionEnvio = address,
                listaProductos = productsList,
                paymentType = paymentType
            )
            val wasSuccess = FirebaseUtils.addSell(request)
            if (wasSuccess) {
                FirebaseUtils.deleteCart(cartId)
                toastMessage.value = "Compra realizada"
            } else
                toastMessage.value = "Ocurrió un error"
            _sellWasSuccess.value = wasSuccess
        }
    }
}