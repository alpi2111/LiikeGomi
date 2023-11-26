package com.liike.liikegomi.main.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory
import com.liike.liikegomi.login.view_model.LoginViewModel

class MainViewModelFactory : BaseViewModelFactory<LoginViewModel>() {
    override fun setViewModel(): LoginViewModel {
        return LoginViewModel()
    }
}