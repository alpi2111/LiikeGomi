package com.liike.liikegomi.shopping_cart.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityShoppingCartBinding
import com.liike.liikegomi.shopping_cart.view_model.ShoppingCartViewModel
import com.liike.liikegomi.shopping_cart.view_model.ShoppingCartViewModelFactory

class ShoppingCartActivity: BaseActivity<ActivityShoppingCartBinding, ShoppingCartViewModel>() {

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, ShoppingCartActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: ShoppingCartViewModel
        get() = getViewModelFactory(this, ShoppingCartViewModelFactory())

    override fun inflate(): ActivityShoppingCartBinding {
        return ActivityShoppingCartBinding.inflate(layoutInflater)
    }
}