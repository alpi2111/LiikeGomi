package com.liike.liikegomi.my_purchases.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Usuarios
import com.liike.liikegomi.background.firebase_db.entities.Ventas
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.background.utils.DateUtils
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch
import java.util.Date

class MyPurchasesViewModel : BaseViewModel() {

    private val _purchasesGrouped = MutableLiveData<Map<Date, List<Ventas>>>()
    val mPurchasesGrouped: LiveData<Map<Date, List<Ventas>>> = _purchasesGrouped
    private val _allPurchases = MutableLiveData<Map<Date, List<Pair<Usuarios, Ventas>>>>()
    val mAllPurchases: LiveData<Map<Date, List<Pair<Usuarios, Ventas>>>> = _allPurchases

    fun getMyPurchases() {
        viewModelScope.launch {
            progressMessage.value = "Obteniendo compras"
            val userId = SharedPrefs.string(SharedPreferenceKeys.USER_ID)!!
            val purchases = FirebaseUtils.getPurchasesByUserId(userId)
            if (purchases.isNullOrEmpty()) {
                toastMessage.value = "No se encontraron compras"
                progressMessage.value = null
                return@launch
            }
            val purchasesGrouped = purchases.groupBy {
                DateUtils.getFormattedDateFromTimestamp(it.fechaCompra)
            }
            _purchasesGrouped.value = purchasesGrouped
            progressMessage.value = null
        }
    }

    fun getAllPurchases() {
        viewModelScope.launch {
            progressMessage.value = "Obteniendo ventas"
            FirebaseUtils.getAllPurchases().let { allPurchases ->
                if (allPurchases == null) {
                    toastMessage.value = "Ocurrió un error al buscar todas las ventas"
                } else {
                    val grouped = allPurchases.groupBy { (_, sell) ->
                        DateUtils.getFormattedDateFromTimestamp(sell.fechaCompra)
                    }
                    _allPurchases.value = grouped
                }
                progressMessage.value = null
            }
        }
    }
}