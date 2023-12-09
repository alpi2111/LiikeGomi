package com.liike.liikegomi.my_purchases.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityMyPurchasesBinding
import com.liike.liikegomi.my_purchases.view_model.MyPurchasesViewModel
import com.liike.liikegomi.my_purchases.view_model.MyPurchasesViewModelFactory

class MyPurchasesActivity: BaseActivity<ActivityMyPurchasesBinding, MyPurchasesViewModel>() {

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, MyPurchasesActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: MyPurchasesViewModel
        get() = getViewModelFactory(this, MyPurchasesViewModelFactory())

    override fun inflate(): ActivityMyPurchasesBinding {
        return ActivityMyPurchasesBinding.inflate(layoutInflater)
    }

}