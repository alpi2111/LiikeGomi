package com.liike.liikegomi.login.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class LoginViewModelFactory : BaseViewModelFactory<LoginViewModel>() {
    override fun setViewModel(): LoginViewModel {
        return LoginViewModel()
    }
}