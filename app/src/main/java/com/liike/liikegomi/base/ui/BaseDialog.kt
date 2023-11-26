package com.liike.liikegomi.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.liike.liikegomi.R

abstract class BaseDialog<T : ViewBinding> : DialogFragment() {

    init {
        this.isCancelable = false
        this.setStyle(STYLE_NO_FRAME, R.style.DialogFragment)
    }

    lateinit var mBinding: T
        private set

    abstract fun inflate(): T
    abstract fun onCreate(view: View, savedInstanceState: Bundle?)

    override fun onStart() {
        super.onStart()
        setLayoutAsWrap()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = inflate()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreate(view, savedInstanceState)
    }

    fun setLayoutAsMatch() {
        setLayout(Layout.MATCH, Layout.MATCH)
    }

    fun setLayoutAsWrap() {
        setLayout(Layout.WRAP, Layout.WRAP)
    }

    fun setLayout(width: Layout, height: Layout) {
        dialog?.window?.setLayout(width.size, height.size)
    }

    sealed class Layout(val size: Int) {
        data object WRAP : Layout(WindowManager.LayoutParams.MATCH_PARENT)
        data object MATCH : Layout(WindowManager.LayoutParams.MATCH_PARENT)
        data class CUSTOM(val param: Int) : Layout(param)
    }
}