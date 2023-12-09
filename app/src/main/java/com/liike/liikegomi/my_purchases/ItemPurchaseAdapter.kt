package com.liike.liikegomi.my_purchases

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liike.liikegomi.background.firebase_db.entities.Ventas
import com.liike.liikegomi.background.utils.DateUtils
import com.liike.liikegomi.databinding.ItemPurchaseBinding
import com.liike.liikegomi.my_purchases.ui.components.ItemPurchaseDetail
import java.util.Date

class ItemPurchaseAdapter : RecyclerView.Adapter<ItemPurchaseAdapter.ViewHolder>() {

    private val mList: MutableList<Pair<Date, List<Ventas>>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int = mList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    fun setData(newList: Map<Date, List<Ventas>>) {
        val currentSize = itemCount
        mList.clear()
        newList.map {
            mList.add(it.key to it.value)
        }
//        newList.reduce { key, a, element ->
//            Log.d("reduce", "$key ($a - $element)")
//            mList.add(key to element)
//            element
//        }
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, itemCount)
    }

    class ViewHolder private constructor(val mBinding: ItemPurchaseBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(item: Pair<Date, List<Ventas>>) {
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
//                venta.listaProductos.forEach {
//                }
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