package com.liike.liikegomi.administrate_products.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liike.liikegomi.administrate_products.view_model.AdminProductsViewModel
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.databinding.ItemAdminProductBinding

class AdminProductsAdapter(private val viewModel: AdminProductsViewModel) : RecyclerView.Adapter<AdminProductsAdapter.ViewHolder>() {

    private val mProductsList: MutableList<Productos> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mProductsList[position], viewModel)
    }

    override fun getItemCount(): Int = mProductsList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    fun setData(products: List<Productos>) {
        val currentSize = itemCount
        mProductsList.clear()
        mProductsList.addAll(products)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, itemCount)
    }

    class ViewHolder(private val mBinding: ItemAdminProductBinding): RecyclerView.ViewHolder(mBinding.root) {

        fun bind(product: Productos, viewModel: AdminProductsViewModel) {
            mBinding.run {

            }
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val binding = ItemAdminProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}