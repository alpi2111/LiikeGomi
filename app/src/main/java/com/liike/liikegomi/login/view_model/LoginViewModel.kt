package com.liike.liikegomi.login.view_model

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.liike.liikegomi.background.database.Dao
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class LoginViewModel: BaseViewModel() {

    private val _wasLogged = MutableLiveData<Boolean>()
    val mWasLogged: LiveData<Boolean> = _wasLogged

    fun login(activity: AppCompatActivity, userName: String, password: String) {
        viewModelScope.launch {
            progressMessage.value = "Iniciando sesión"
            FirebaseUtils.loginWithUserNameAndPassword(activity, userName, password) { loginSuccess, message ->
                progressMessage.value = null
                toastMessage.value = message
                _wasLogged.value = loginSuccess

            }
        }
    }

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            progressMessage.value = "Iniciando sesión"
            val user = Dao.getUserByUserName(userName, password)
            delay(Random.nextLong(1000, 4000))
            if (user == null)
                toastMessage.value = "No se pudo iniciar sesión, verifica tu usuario y contraseña"
            else
                _wasLogged.value = true
            progressMessage.value = null
        }
    }
}