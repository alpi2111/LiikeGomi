package com.liike.liikegomi.add_category.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class AddCategoryViewModelFactory : BaseViewModelFactory<AddCategoryViewModel>() {
    override fun setViewModel(): AddCategoryViewModel {
        return AddCategoryViewModel()
    }
}