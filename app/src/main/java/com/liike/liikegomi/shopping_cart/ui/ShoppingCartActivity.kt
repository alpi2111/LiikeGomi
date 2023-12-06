package com.liike.liikegomi.shopping_cart.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.mCartData.observe(this) { cart ->
            if (cart == null) {
                hideProgress()
                mBinding.noCartItems.isVisible = true
                mBinding.total.text = "$0.00"
                return@observe
            }
            if (mViewModel.cartIsNotValid(cart)) {
                hideProgress()
                mBinding.noCartItems.text = "Carrito no válido"
                mBinding.noCartItems.isVisible = true
                return@observe
            }
            hideProgress()
            mBinding.btnPay.isEnabled = true
        }
        mViewModel.getShoppingCart()
    }

    private fun hideProgress() {
        mBinding.progressBar.isVisible = false
    }
}