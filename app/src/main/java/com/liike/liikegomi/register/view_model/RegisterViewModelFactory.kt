package com.liike.liikegomi.register.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class RegisterViewModelFactory : BaseViewModelFactory<RegisterViewModel>() {
    override fun setViewModel(): RegisterViewModel {
        return RegisterViewModel()
    }
}