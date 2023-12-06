package com.liike.liikegomi.base.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.toObject
import com.liike.liikegomi.background.firebase_db.FirebaseUtils
import com.liike.liikegomi.background.firebase_db.entities.Usuarios
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {
    protected val progressMessage = MutableLiveData<String?>()
    val mProgressMessage: LiveData<String?> = progressMessage

    protected val toastMessage = MutableLiveData<String?>()
    val mToastMessage: LiveData<String?> = toastMessage

    private val _closeSession = MutableLiveData<Boolean>()
    val mCloseSession: LiveData<Boolean> = _closeSession

    private val _isCartEmpty = MutableLiveData<Boolean>()
    val mIsCartEmpty: LiveData<Boolean> = _isCartEmpty

    fun listenForLoginOrActivatedUser(activity: AppCompatActivity) {
        FirebaseUtils.getUserDocumentReference()?.addSnapshotListener(activity) { document, _ ->
            val user = document?.toObject<Usuarios>()
            if (user?.isLogged == false) {
                viewModelScope.launch {
                    toastMessage.value = "Su sesión ha sido cerrada remotamente"
                    _closeSession.value = true
                }
                return@addSnapshotListener
            }

            if (user?.isActive == false) {
                viewModelScope.launch {
                    toastMessage.value = "Su cuenta ha sido deshabilitada, sesión cerrada"
                    _closeSession.value = true
                }
                return@addSnapshotListener
            }
        }
    }

    fun closeSession() {
        progressMessage.value = "Cerrando sesión"
        FirebaseUtils.closeCurrentSession { sessionClosed, message ->
            toastMessage.value = message
            progressMessage.value = null
            _closeSession.value = sessionClosed
        }
    }

    fun verifyCartEmpty() {
        viewModelScope.launch {
            val isEmpty = FirebaseUtils.isCartEmpty()
            _isCartEmpty.value = isEmpty
        }
    }

//    fun getDao(): AppDatabase {
//        return Dao.getInstance()
//    }
}