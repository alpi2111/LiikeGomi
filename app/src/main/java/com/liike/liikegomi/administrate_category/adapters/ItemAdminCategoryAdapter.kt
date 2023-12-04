package com.liike.liikegomi.administrate_category.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liike.liikegomi.administrate_category.view_model.AdminCategoryViewModel
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.databinding.ItemAdminCategoryBinding
import com.liike.liikegomi.text

class ItemAdminCategoryAdapter(private val viewModel: AdminCategoryViewModel): RecyclerView.Adapter<ItemAdminCategoryAdapter.ViewHolder>() {

    private val mCategoriesList: MutableList<Categoria> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mCategoriesList[position], viewModel)
    }

    override fun getItemCount(): Int = mCategoriesList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    fun setData(categories: List<Categoria>) {
        val currentSize = itemCount
        mCategoriesList.clear()
        mCategoriesList.addAll(categories)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, mCategoriesList.size)
    }

    class ViewHolder(private val mBinding: ItemAdminCategoryBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(category: Categoria, viewModel: AdminCategoryViewModel) {
            mBinding.run {
                etCategoryTitle.setText(category.category)
                ilCategoryTitle.helperText = "ID: ${category.idCategory}"
                switchIsVisible.setOnCheckedChangeListener { _, isChecked ->
                    category.isVisible = isChecked
                    viewModel.updateCategory(category)
                }
                btnUpdateName.setOnClickListener {
                    if (etCategoryTitle.text() == category.category) {
                        MessageUtils.toast(itemView.context, "El nombre no ha cambiado")
                        return@setOnClickListener
                    }
                    category.category = etCategoryTitle.text()
                    viewModel.updateCategory(category)
                }
                btnDelete.setOnClickListener {
                    viewModel.deleteCategory(category.idFirebaseCategory, adapterPosition)
                }
            }
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val binding = ItemAdminCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}