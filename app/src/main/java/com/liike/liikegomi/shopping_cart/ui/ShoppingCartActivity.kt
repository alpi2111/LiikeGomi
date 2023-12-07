package com.liike.liikegomi.shopping_cart.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.liike.liikegomi.background.firebase_db.entities.Carrito
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityShoppingCartBinding
import com.liike.liikegomi.shopping_cart.adapters.CartProductsAdapter
import com.liike.liikegomi.shopping_cart.adapters.CartProductsCallback
import com.liike.liikegomi.shopping_cart.view_model.ShoppingCartViewModel
import com.liike.liikegomi.shopping_cart.view_model.ShoppingCartViewModelFactory

class ShoppingCartActivity: BaseActivity<ActivityShoppingCartBinding, ShoppingCartViewModel>(), CartProductsCallback {

    private lateinit var mCartAdapter: CartProductsAdapter
    private var mCart: Carrito? = null

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

        mCartAdapter = CartProductsAdapter(this)
        mBinding.recyclerViewCart.adapter = mCartAdapter

        mViewModel.mCartData.observe(this) { cart ->
            mCart = cart
            if (cart == null) {
                hideProgress()
                mBinding.noCartItems.isVisible = true
                mBinding.total.text = "$0.00"
                mBinding.btnPay.isEnabled = false
                mBinding.btnPay.text = "Pagar"
                mBinding.recyclerViewCart.visibility = View.INVISIBLE
                return@observe
            }
            if (mViewModel.cartIsNotValid(cart)) {
                hideProgress()
                mBinding.noCartItems.text = "Carrito no válido"
                mBinding.noCartItems.isVisible = true
                mBinding.btnPay.isEnabled = false
                mBinding.btnPay.text = "Pagar"
                mBinding.recyclerViewCart.visibility = View.INVISIBLE
                return@observe
            }
            if (cart.productos.isNullOrEmpty()) {
                hideProgress()
                mBinding.noCartItems.isVisible = true
                mBinding.total.text = "$0.00"
                mBinding.btnPay.isEnabled = false
                mBinding.btnPay.text = "Pagar"
                mBinding.recyclerViewCart.visibility = View.INVISIBLE
                return@observe
            }
            hideProgress()
            mBinding.noCartItems.isVisible = false
            var total = 0.0
            mCartAdapter.setData(cart.productos!!)
            mBinding.recyclerViewCart.visibility = View.VISIBLE
            cart.productos?.forEach {
                total += it.precioUnidad!! * it.cantidad!!
            }
            mBinding.total.text = "$$total"
            mBinding.btnPay.isEnabled = true
            mBinding.btnPay.text = "Pagar (${cart.productos?.size ?: 0} producto(s))"
        }
        mViewModel.getShoppingCart()
    }

    private fun hideProgress() {
        mBinding.progressBar.isVisible = false
    }

    override fun onUpdateCart(cartProduct: Carrito.Producto) {
        if (getCartForUpdate(cartProduct)) return
        mCartAdapter.updateItem(cartProduct)
        updateProductValues()
        mViewModel.updateCart(mCart!!)
    }

    override fun onRemoveCart(cartProduct: Carrito.Producto) {
        if (getCartForUpdate(cartProduct)) return
        if (cartProduct.cantidad!! <= 0) {
            mViewModel.deleteCartItemProduct(mCart!!)
            return
        }
        mCartAdapter.updateItem(cartProduct)
        updateProductValues()
        mViewModel.updateCart(mCart!!)
    }

    override fun onDeleteCart(cartProduct: Carrito.Producto, position: Int) {
        if (getCartForUpdate(cartProduct)) return
        mViewModel.deleteCartItemProduct(mCart!!)
    }

    private fun getCartForUpdate(cartProduct: Carrito.Producto): Boolean {
        val products = mCart?.productos ?: run {
            MessageUtils.toast(this@ShoppingCartActivity, "No se pudo obtebner el carrito")
            return true
        }

        val cartProductIndex = products.indexOfFirst { it.idProducto == cartProduct.idProducto }
        if (cartProductIndex > -1) {
            val toUpdate = products.toMutableList()
            toUpdate[cartProductIndex] = cartProduct
            mCart!!.productos = toUpdate
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun updateProductValues() {
        var total = 0.0
        var size = 0
        mCart?.productos?.forEach {
            size++
            total += it.precioUnidad!! * it.cantidad!!
        }
        mBinding.total.text = "$$total"
        mBinding.btnPay.text = "Pagar ($size producto(s))"
    }

}