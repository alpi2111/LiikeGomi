package com.liike.liikegomi.my_purchases

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liike.liikegomi.background.firebase_db.entities.Usuarios
import com.liike.liikegomi.background.firebase_db.entities.Ventas
import com.liike.liikegomi.background.utils.CryptUtils
import com.liike.liikegomi.background.utils.DateUtils
import com.liike.liikegomi.databinding.ItemPurchaseBinding
import com.liike.liikegomi.my_purchases.ui.components.ItemPurchaseDetail
import java.util.Date

class ItemPurchaseAdapter(private val mComesFromAdmin: Boolean) : RecyclerView.Adapter<ItemPurchaseAdapter.ViewHolder>() {

    private val mListUser: MutableList<Pair<Date, List<Ventas>>> = mutableListOf()
    private val mListAdmin: MutableList<Pair<Date, List<Pair<Usuarios, Ventas>>>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mComesFromAdmin)
            holder.bindAdmin(mListAdmin[position])
        else
            holder.bindUser(mListUser[position])
    }

    override fun getItemCount(): Int {
        return if (mComesFromAdmin)
            mListAdmin.size
        else
            mListUser.size
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    fun setDataUser(newList: Map<Date, List<Ventas>>) {
        val currentSize = itemCount
        mListUser.clear()
        newList.map {
            mListUser.add(it.key to it.value)
        }
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, itemCount)
    }

    fun setDataAdmin(newList: Map<Date, List<Pair<Usuarios, Ventas>>>) {
        val currentSize = itemCount
        mListAdmin.clear()
        newList.map { (date, list) ->
            mListAdmin.add(date to list)
        }
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, itemCount)
    }


    class ViewHolder private constructor(val mBinding: ItemPurchaseBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bindUser(item: Pair<Date, List<Ventas>>) {
            val (date, venta) = item
            mBinding.run {
                purchaseDate.text = DateUtils.getFormattedDateAsString(date)
                venta.forEach {
                    it.listaProductos.forEach { producto ->
                        val newItem = ItemPurchaseDetail(itemView.context).apply {
                            setData(producto.nombre!!, (producto.cantidad!! * producto.precioUnidad!!), producto.cantidad!!)
                        }
                        layoutItems.addView(newItem)
                    }
                }
                layoutItems.requestLayout()
            }
        }

        fun bindAdmin(item: Pair<Date, List<Pair<Usuarios, Ventas>>>) {
            val (date, venta) = item
            mBinding.run {
                purchaseDate.text = DateUtils.getFormattedDateAsString(date)
                venta.forEach { (user, sell) ->
                    sell.listaProductos.forEach { producto ->
                        val newItem = ItemPurchaseDetail(itemView.context).apply {
                            val fullUserName = "${user.name} ${user.lastName} - ${CryptUtils.decrypt(user.email)}"
                            setData(producto.nombre!!, (producto.cantidad!! * producto.precioUnidad!!), producto.cantidad!!, fullUserName)
                        }
                        layoutItems.addView(newItem)
                    }
                }
                layoutItems.requestLayout()
            }
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val binding = ItemPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}