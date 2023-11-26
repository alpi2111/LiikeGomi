package com.liike.liikegomi.background.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.liike.liikegomi.background.utils.ui.ProgressDialog

object MessageUtils {
    private val lock = Any()
    private var mToast: Toast? = null

    fun toast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        synchronized(lock) {
            if (mToast != null)
                mToast!!.cancel()
            mToast = Toast.makeText(context, message, duration).also { it.show() }
        }
    }

    fun progress(fragmentManager: FragmentManager, message: String) {
        val currentFragment = fragmentManager.findFragmentByTag(ProgressDialog.TAG) as? ProgressDialog
        currentFragment?.dismiss()
        ProgressDialog.show(fragmentManager, message)
    }

    fun closeProgress(fragmentManager: FragmentManager) {
        val currentFragment = fragmentManager.findFragmentByTag(ProgressDialog.TAG) as? ProgressDialog
        currentFragment?.dismiss()
    }
}