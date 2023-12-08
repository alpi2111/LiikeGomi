package com.liike.liikegomi.add_address.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class AddAddressViewModelFactory: BaseViewModelFactory<AddAddressViewModel>() {
    override fun setViewModel(): AddAddressViewModel {
        return AddAddressViewModel()
    }
}