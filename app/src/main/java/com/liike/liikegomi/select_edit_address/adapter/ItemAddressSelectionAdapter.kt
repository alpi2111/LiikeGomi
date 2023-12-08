package com.liike.liikegomi.select_edit_address.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liike.liikegomi.background.firebase_db.entities.Direcciones
import com.liike.liikegomi.databinding.ItemAddressBinding
import kotlin.properties.Delegates

class ItemAddressSelectionAdapter : RecyclerView.Adapter<ItemAddressSelectionAdapter.ViewHolder>(), RadioButtonSelectedCallback {

    private val mList: MutableList<Direcciones> = mutableListOf()
    private var selectedPosition by Delegates.observable(0) { _, oldPos, newPos ->
        if (newPos in mList.indices) {
            notifyItemChanged(oldPos, "changed")
            notifyItemChanged(newPos, "changed")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position], position == selectedPosition, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else
            holder.updateSelection(position == selectedPosition)
    }

    override fun getItemCount(): Int = mList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    override fun onRadioSelection(position: Int) {
        selectedPosition = position
    }

    fun setData(newData: List<Direcciones>) {
        val currentSize = itemCount
        mList.clear()
        mList.addAll(newData)
        notifyItemRangeRemoved(0, currentSize)
        notifyItemRangeInserted(0, currentSize)
    }

    fun getSelectedAddress(): Direcciones {
        return mList[selectedPosition]
    }

    fun addAndSelectLastAddress(newAddress: Direcciones) {
        mList.add(newAddress)
        notifyItemInserted(mList.lastIndex)
    }

    class ViewHolder private constructor(private val mBinding: ItemAddressBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(address: Direcciones, isSelected: Boolean, buttonSelectedCallback: RadioButtonSelectedCallback) {
            mBinding.run {
                radioButtonAddress.text = address.calle
                fullAddress.text = address.formattedAddress()
                radioButtonAddress.isChecked = isSelected
                radioButtonAddress.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked)
                        buttonSelectedCallback.onRadioSelection(adapterPosition)
                }
            }
        }

        fun updateSelection(isSelected: Boolean) {
            mBinding.radioButtonAddress.isChecked = isSelected
        }

        companion object {
            fun createViewHolder(parent: ViewGroup): ViewHolder {
                val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }
}