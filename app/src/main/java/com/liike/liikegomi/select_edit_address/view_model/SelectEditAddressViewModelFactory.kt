package com.liike.liikegomi.select_edit_address.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class SelectEditAddressViewModelFactory: BaseViewModelFactory<SelectEditAddressViewModel>() {
    override fun setViewModel(): SelectEditAddressViewModel {
        return SelectEditAddressViewModel()
    }
}