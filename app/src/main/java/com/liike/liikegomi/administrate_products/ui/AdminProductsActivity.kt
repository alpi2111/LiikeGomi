package com.liike.liikegomi.administrate_products.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.liike.liikegomi.administrate_products.adapters.AdminProductsViewPagerAdapter
import com.liike.liikegomi.administrate_products.view_model.AdminProductsViewModel
import com.liike.liikegomi.administrate_products.view_model.AdminProductsViewModelFactory
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityAdministrateProductsBinding

class AdminProductsActivity : BaseActivity<ActivityAdministrateProductsBinding, AdminProductsViewModel>() {

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, AdminProductsActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: AdminProductsViewModel
        get() = getViewModelFactory(this, AdminProductsViewModelFactory())

    override fun inflate(): ActivityAdministrateProductsBinding {
        return ActivityAdministrateProductsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.mCategoriesList.observe(this) {
            val adapter = AdminProductsViewPagerAdapter(it, supportFragmentManager, lifecycle)
            mBinding.viewPagerProducts.adapter = adapter
            TabLayoutMediator(mBinding.tabLayoutProducts, mBinding.viewPagerProducts) { tab, pos ->
                tab.text = it[pos].category
            }.attach()
        }

        mViewModel.getCategories()
    }
}