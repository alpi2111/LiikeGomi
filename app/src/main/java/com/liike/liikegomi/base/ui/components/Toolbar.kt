package com.liike.liikegomi.base.ui.components

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.Toolbar
import com.liike.liikegomi.R
import com.liike.liikegomi.databinding.LayoutToolbarBinding

class Toolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : Toolbar(context, attrs) {

    private val mActivity: AppCompatActivity? by lazy { this.context.getActivity() }
    private var mBinding: LayoutToolbarBinding
    private var mOnBackPressedDispatcher: OnBackPressedDispatcher? = null
    private var mTitle: String

    init {
        this.setContentInsetsRelative(0, 0)
        mBinding = LayoutToolbarBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater, this, true)
        context.obtainStyledAttributes(attrs, R.styleable.Toolbar, 0, 0).apply {
            mTitle = getText(androidx.appcompat.R.styleable.Toolbar_title)?.toString() ?: "No title"
            recycle()
        }
        if (isInEditMode) {
            mBinding.toolbarTitle.title = mTitle
        }
    }

    fun configure(dispatcher: OnBackPressedDispatcher, canShowBackButton: Boolean, title: String? = null) {
        mOnBackPressedDispatcher = dispatcher
        mActivity.let {
            if (it == null)
                return@let
            it.setSupportActionBar(mBinding.root)
            it.supportActionBar?.run {
                setDisplayShowTitleEnabled(true)
                setDisplayHomeAsUpEnabled(canShowBackButton)
                setDisplayShowHomeEnabled(canShowBackButton)
                setHomeButtonEnabled(canShowBackButton)
                this.title = title ?: mTitle
            }
        }
        mBinding.toolbarTitle.setNavigationOnClickListener {
            mOnBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun Context.getActivity(): AppCompatActivity? {
        if (isInEditMode) return AppCompatActivity()
        return when (this) {
            is AppCompatActivity -> this
            is ContextThemeWrapper -> this.baseContext as AppCompatActivity
            is ContextWrapper -> this.baseContext as AppCompatActivity
            else -> null
        }
    }
}