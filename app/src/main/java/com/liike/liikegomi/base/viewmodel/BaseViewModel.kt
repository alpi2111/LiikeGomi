package com.liike.liikegomi.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    protected var progressMessage = MutableLiveData<String?>()
    val mProgressMessage: LiveData<String?> = progressMessage

    protected var toastMessage = MutableLiveData<String?>()
    val mToastMessage: LiveData<String?> = toastMessage

//    fun getDao(): AppDatabase {
//        return Dao.getInstance()
//    }
}