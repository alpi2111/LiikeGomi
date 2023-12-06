package com.liike.liikegomi.shopping_cart.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Carrito
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class ShoppingCartViewModel : BaseViewModel() {

    private val _cartData = MutableLiveData<Carrito?>()
    val mCartData: LiveData<Carrito?> = _cartData

    fun getShoppingCart() {
        viewModelScope.launch {
            val idUser = SharedPrefs.string(SharedPreferenceKeys.USER_ID)
            if (idUser == null) {
                toastMessage.value = "ID de usuario no válido, reiniciando sesión"
                return@launch
            }
            val cart = FirebaseUtils.getCartByUser(idUser)
            if (cart != null && cartIsNotValid(cart)) {
                toastMessage.value = "Parece que el usuario tiene más de un carrito, contacta con soporte"
            }
            _cartData.value = cart
        }
    }

    fun cartIsNotValid(cart: Carrito): Boolean {
        return cart.idFirebaseCarrito.isNullOrBlank() or cart.nombreUsuario.isNullOrBlank()
    }
}