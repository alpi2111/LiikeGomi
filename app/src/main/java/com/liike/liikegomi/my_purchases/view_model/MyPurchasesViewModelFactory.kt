package com.liike.liikegomi.my_purchases.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class MyPurchasesViewModelFactory: BaseViewModelFactory<MyPurchasesViewModel>() {
    override fun setViewModel(): MyPurchasesViewModel {
        return MyPurchasesViewModel()
    }
}