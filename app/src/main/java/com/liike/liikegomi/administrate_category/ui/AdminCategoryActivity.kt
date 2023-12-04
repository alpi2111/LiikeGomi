package com.liike.liikegomi.administrate_category.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.liike.liikegomi.administrate_category.adapters.ItemAdminCategoryAdapter
import com.liike.liikegomi.administrate_category.view_model.AdminCategoryViewModel
import com.liike.liikegomi.administrate_category.view_model.AdminCategoryViewModelFactory
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityAdministrateCategoryBinding

class AdminCategoryActivity : BaseActivity<ActivityAdministrateCategoryBinding, AdminCategoryViewModel>() {

    private lateinit var mCategoriesAdapter: ItemAdminCategoryAdapter

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, AdminCategoryActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: AdminCategoryViewModel
        get() = ViewModelProvider(this, AdminCategoryViewModelFactory())[AdminCategoryViewModel::class.java]

    override fun inflate(): ActivityAdministrateCategoryBinding {
        return ActivityAdministrateCategoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCategoriesAdapter = ItemAdminCategoryAdapter()
        mBinding.recyclerViewCategories.adapter = mCategoriesAdapter

        mViewModel.mCategories.observe(this) { categoriesList ->
            mCategoriesAdapter.setData(categoriesList)
        }

        mViewModel.getCategories()
    }
}