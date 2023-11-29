package com.liike.liikegomi.register.view_model

import androidx.lifecycle.viewModelScope
import com.liike.liikegomi.background.database.Dao
import com.liike.liikegomi.background.firebase_db.entities.Usuarios
import com.liike.liikegomi.background.utils.RandomUtils
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class RegisterViewModel: BaseViewModel() {

    fun getUserRol() {
    }

    fun saveUser(user: Usuarios) {
        viewModelScope.launch {
            progressMessage.value = "Creando usuario"
            val userRol = Dao.getInstance().rolDao().getRol()
            if (userRol == null || userRol < 0) {
                progressMessage.value = null
                toastMessage.value = "Rol de usuario inexistente"
                cancel()
                ensureActive()
                return@launch
            }
            user.idRol = userRol
            delay(RandomUtils.getSimulationSeconds())
            Dao.saveUser(user)
            progressMessage.value = null
            toastMessage.value = "Cuenta creada"
        }
    }
}