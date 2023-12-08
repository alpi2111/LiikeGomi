package com.liike.liikegomi.payment.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class PaymentViewModelFactory: BaseViewModelFactory<PaymentViewModel>() {
    override fun setViewModel(): PaymentViewModel {
        return PaymentViewModel()
    }
}