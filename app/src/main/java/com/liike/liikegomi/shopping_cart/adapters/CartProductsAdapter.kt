package com.liike.liikegomi.shopping_cart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liike.liikegomi.R
import com.liike.liikegomi.background.firebase_db.entities.Carrito
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.databinding.ItemCartBinding

class CartProductsAdapter(private val callback: CartProductsCallback) : RecyclerView.Adapter<CartProductsAdapter.ViewHolder>() {

    private val mCarritoList: MutableList<Carrito.Producto> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mCarritoList[position], callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else
            holder.updateQuantity(mCarritoList[position])
    }

    override fun getItemCount(): Int = mCarritoList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    fun setData(carritoList: List<Carrito.Producto>) {
        val currentSize = itemCount
        mCarritoList.clear()
        mCarritoList.addAll(carritoList)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, itemCount)
    }

    fun deleteItem(position: Int) {
        if (position in 0..itemCount) {
            mCarritoList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateItem(carritoProduct: Carrito.Producto) {
        val cartItem = mCarritoList.indexOfFirst { it.idProducto == carritoProduct.idProducto }
        if (cartItem != -1)
            notifyItemChanged(cartItem, "update")
    }

    class ViewHolder private constructor(private val mBinding: ItemCartBinding): RecyclerView.ViewHolder(mBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(cartProduct: Carrito.Producto, callback: CartProductsCallback) {
            mBinding.run {
                productName.text = cartProduct.nombre
                productPrice.text = "$${cartProduct.precioUnidad}"
                itemCount.text = "${cartProduct.cantidad}"
//                val bytes = cartProduct.imagen?.toBytes()
//                if (bytes != null) {
                productImage.setPadding(0, 0, 0, 0)
                productImage.imageTintList = null
                Glide.with(itemView.context).load(R.drawable.ic_image_search).centerCrop().into(productImage)
//                }

                btnAdd.setOnClickListener {
                    callback.onUpdateCart(cartProduct.apply { cantidad = cantidad!! + 1 })
                }

                btnRemove.setOnClickListener {
                    callback.onRemoveCart(cartProduct.apply { cantidad = cantidad!! - 1 })
                }

                btnDelete.setOnClickListener {
                    MessageUtils.dialog(
                        context = itemView.context,
                        title = "Advertencia",
                        message = "¿Realmente desas borrar el producto ${cartProduct.nombre} del carrito?",
                        okButton = "Sí",
                        cancelButton = "No",
                        onOkAction = {
                            callback.onDeleteCart(cartProduct.apply { cantidad = 0 }, adapterPosition)
                        },
                        onCancelAction = {}
                    )
                }
            }
        }

        fun updateQuantity(cartProduct: Carrito.Producto) {
            mBinding.run {
                itemCount.text = "${cartProduct.cantidad}"
            }
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}