package com.liike.liikegomi.administrate_category.view_model

import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory

class AdminCategoryViewModelFactory: BaseViewModelFactory<AdminCategoryViewModel>() {
    override fun setViewModel(): AdminCategoryViewModel {
        return AdminCategoryViewModel()
    }
}