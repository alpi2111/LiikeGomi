package com.liike.liikegomi.shopping_cart.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class ShoppingCartViewModelFactory: BaseViewModelFactory<ShoppingCartViewModel>() {
    override fun setViewModel(): ShoppingCartViewModel {
        return ShoppingCartViewModel()
    }
}