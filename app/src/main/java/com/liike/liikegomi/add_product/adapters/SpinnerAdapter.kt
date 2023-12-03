package com.liike.liikegomi.add_product.adapters

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import com.liike.liikegomi.R

class SpinnerAdapter<T> constructor(
    private val context: Context,
    title: T,
    private var items: List<T>,
    @LayoutRes private val layout: Int = R.layout.simple_spinner_item,
) : BaseAdapter() {

    init {
        val temp = items.toMutableList()
        temp.add(0, title)
        items = temp
    }

    @get:ColorInt
    private val activeTextColor: Int by lazy {
        val typedValue = TypedValue()
        val attrFound = context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
        if (attrFound) {
            typedValue.data
        } else {
            context.getColor(R.color.light_primary)
        }
    }
    private val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): T = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        return createViewFromResource(layoutInflater, position, view, viewGroup, layout)
    }

    override fun isEnabled(position: Int): Boolean {
        if (position == 0)
            return false
        return true
    }

    private fun createViewFromResource(inflater: LayoutInflater, position: Int, convertView: View?, parent: ViewGroup?, resource: Int): View {
        val text: TextView?
        val view: View = convertView ?: inflater.inflate(resource, parent, false)
        try {
            text = view as TextView
        } catch (e: ClassCastException) {
            Log.e("SpinnerAdapter", "You must supply a resource ID for a TextView")
            throw IllegalStateException("SpinnerAdapter requires the resource ID to be a TextView", e)
        }
        val item = getItem(position)
        if (item is CharSequence) {
            text.text = item
        } else {
            text.text = item.toString()
        }
        if (position != 0)
            text.setTextColor(activeTextColor)
        return view
    }
}