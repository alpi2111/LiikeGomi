package com.liike.liikegomi.register.view_model

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.liike.liikegomi.background.database.Dao
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Usuarios
import com.liike.liikegomi.background.utils.RandomUtils
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class RegisterViewModel: BaseViewModel() {

    private val _wasRegistered: MutableLiveData<Boolean> = MutableLiveData()
    val mWasRegistered: LiveData<Boolean> = _wasRegistered

    fun saveUser(activity: AppCompatActivity, user: Usuarios) {
        viewModelScope.launch {
            progressMessage.value = "Creando usuario"
            FirebaseUtils.userCanCreateAnAccount(activity, user.userName, user.email) { creationSuccess, message ->
                if (!creationSuccess) {
                    progressMessage.value = null
                    toastMessage.value = message
                    return@userCanCreateAnAccount
                }
                FirebaseUtils.saveUser(activity, user) { wasSaved ->
                    progressMessage.value = null
                    if (wasSaved) {
                        toastMessage.value = "Usuario creado"
                    } else {
                        toastMessage.value = "Error al crear usuario, intenta nuevamente"
                    }
                    _wasRegistered.value = wasSaved
                }
            }
        }
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
            delay(RandomUtils.getSimulationSeconds())
//            Dao.saveUser(user)
            progressMessage.value = null
            toastMessage.value = "Cuenta creada"
        }
    }
}