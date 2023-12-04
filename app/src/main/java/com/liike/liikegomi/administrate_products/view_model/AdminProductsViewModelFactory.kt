package com.liike.liikegomi.administrate_products.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class AdminProductsViewModelFactory: BaseViewModelFactory<AdminProductsViewModel>() {
    override fun setViewModel(): AdminProductsViewModel {
        return AdminProductsViewModel()
    }
}