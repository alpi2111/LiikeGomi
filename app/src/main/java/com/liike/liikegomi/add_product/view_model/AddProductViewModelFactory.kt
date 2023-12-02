package com.liike.liikegomi.add_product.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class AddProductViewModelFactory : BaseViewModelFactory<AddProductViewModel>() {
    override fun setViewModel(): AddProductViewModel {
        return AddProductViewModel()
    }
}