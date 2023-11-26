package com.liike.liikegomi.main.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class MainViewModelFactory : BaseViewModelFactory<MainViewModel>() {
    override fun setViewModel(): MainViewModel {
        return MainViewModel()
    }
}