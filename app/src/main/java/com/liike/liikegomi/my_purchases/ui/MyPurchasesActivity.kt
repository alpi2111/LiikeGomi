package com.liike.liikegomi.my_purchases.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityMyPurchasesBinding
import com.liike.liikegomi.my_purchases.ItemPurchaseAdapter
import com.liike.liikegomi.my_purchases.view_model.MyPurchasesViewModel
import com.liike.liikegomi.my_purchases.view_model.MyPurchasesViewModelFactory

class MyPurchasesActivity: BaseActivity<ActivityMyPurchasesBinding, MyPurchasesViewModel>() {

    private lateinit var mPurchasesAdapter: ItemPurchaseAdapter

    companion object {
        const val MY_PURCHASES_ADMIN_VIEW_KEY = "comesFromAdminView"
        fun launch(appCompatActivity: AppCompatActivity, comesFromAdminView: Boolean) {
            val intent = Intent(appCompatActivity, MyPurchasesActivity::class.java).apply {
                putExtra(MY_PURCHASES_ADMIN_VIEW_KEY, comesFromAdminView)
            }
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: MyPurchasesViewModel
        get() = getViewModelFactory(this, MyPurchasesViewModelFactory())

    override fun inflate(): ActivityMyPurchasesBinding {
        return ActivityMyPurchasesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val comesFromAdminView = intent.getBooleanExtra(MY_PURCHASES_ADMIN_VIEW_KEY, false)
        if (comesFromAdminView) {
            mBinding.toolbar.title = "Mis ventas"
        }
        mPurchasesAdapter = ItemPurchaseAdapter(comesFromAdminView)
        mBinding.recyclerPurchases.adapter = mPurchasesAdapter

        mViewModel.mAllPurchases.observe(this) {
            mPurchasesAdapter.setDataAdmin(it)
        }

        mViewModel.mPurchasesGrouped.observe(this) {
            mPurchasesAdapter.setDataUser(it)
        }

        if (comesFromAdminView) {
            mViewModel.getAllPurchases()
        } else {
            mViewModel.getMyPurchases()
        }
    }

}