package com.liike.liikegomi.main.ui.adapters

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.databinding.ItemProductMainBinding

class ItemProductAdapter : RecyclerView.Adapter<ItemProductAdapter.ViewHolder>() {

    private val mProductList: MutableList<Productos> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mProductList[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else
            holder.updateProduct(mProductList[position])
    }

    override fun getItemCount(): Int = mProductList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    fun setProducts(products: List<Productos>) {
        val currentSize = itemCount
        mProductList.clear()
        notifyItemRangeRemoved(0, currentSize)
        mProductList.addAll(products)
        notifyItemRangeInserted(0, itemCount)
    }

    fun updateProduct(product: Productos) {
        val indexOfProduct = mProductList.indexOfFirst { it.productId == product.productId }
        if (indexOfProduct == -1) {
            mProductList.add(product)
            notifyItemInserted(mProductList.lastIndex)
            return
        }
        mProductList[indexOfProduct] = product
        notifyItemChanged(indexOfProduct, "updated")
    }

    class ViewHolder(private val mBinding: ItemProductMainBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(product: Productos) {
            setData(product)
        }

        fun updateProduct(product: Productos) {
            setData(product)
        }

        @SuppressLint("SetTextI18n")
        private fun setData(product: Productos) {
            mBinding.run {
                productTitle.text = product.productName
                productDescription.text = product.productDescription
                price.text = "$${product.productPrice}"
                val imageBytes = product.productImage!!.toBytes()
                val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                productImage.setImageBitmap(imageBitmap)
                addCart.setOnClickListener {
                    // TODO: Add to cart, maybe with a callback
                    MessageUtils.toast(this.root.context, "${product.productName} selected")
                }
            }
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val binding = ItemProductMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}