package com.liike.liikegomi.my_purchases.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.liike.liikegomi.databinding.ItemPurchaseDetailBinding

class ItemPurchaseDetail @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {
    private val mBinding: ItemPurchaseDetailBinding

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        mBinding = ItemPurchaseDetailBinding.inflate(LayoutInflater.from(context), this, true)
    }

    @SuppressLint("SetTextI18n")
    fun setData(name: String, totalPayed: Double, quantity: Int) {
        mBinding.run {
            productName.text = name
            productQuantity.text = "Cantidad: $quantity"
            total.text = "Total pagado: $totalPayed"
        }
    }
}