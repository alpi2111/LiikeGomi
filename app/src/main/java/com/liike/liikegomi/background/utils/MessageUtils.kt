package com.liike.liikegomi.background.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentManager
import com.liike.liikegomi.background.utils.ui.ProgressDialog

object MessageUtils {
    private val lock = Any()
    private var mToast: Toast? = null
    private var mAlertDialog: AlertDialog? = null

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

    fun dialog(context: Context, title: String, message: String, okButton: String, onAction: () -> Unit) {
        synchronized(lock) {
            mAlertDialog.let {
                if (it != null && it.isShowing) {
                    it.dismiss()
                }
            }
            AlertDialog.Builder(context).also { dialog ->
                dialog.setCancelable(false)
                dialog.setTitle(title)
                dialog.setMessage(message)
                dialog.setPositiveButton(okButton) { dialogInterface, _ ->
                    onAction.invoke()
                    dialogInterface.dismiss()
                }
                mAlertDialog = dialog.show()
            }
        }
    }

    fun dialog(context: Context, title: String, message: String, okButton: String, cancelButton: String, onOkAction: () -> Unit, onCancelAction: () -> Unit) {
        synchronized(lock) {
            mAlertDialog.let {
                if (it != null && it.isShowing) {
                    it.dismiss()
                }
            }
            AlertDialog.Builder(context).also { dialog ->
                dialog.setCancelable(false)
                dialog.setTitle(title)
                dialog.setMessage(message)
                dialog.setPositiveButton(okButton) { dialogInterface, _ ->
                    onOkAction.invoke()
                    dialogInterface.dismiss()
                }
                dialog.setNegativeButton(cancelButton) { dialogInterface, _ ->
                    onCancelAction.invoke()
                    dialogInterface.dismiss()
                }
                mAlertDialog = dialog.show()
            }
        }
    }

    fun dialogWithIcon(context: Context, title: String, message: String, @DrawableRes icon: Int, okButton: String, onAction: () -> Unit) {
        synchronized(lock) {
            mAlertDialog.let {
                if (it != null && it.isShowing) {
                    it.dismiss()
                }
            }
            AlertDialog.Builder(context).also { dialog ->
                dialog.setCancelable(false)
                dialog.setTitle(title)
                dialog.setMessage(message)
                dialog.setIcon(icon)
                dialog.setPositiveButton(okButton) { dialogInterface, _ ->
                    onAction.invoke()
                    dialogInterface.dismiss()
                }
                mAlertDialog = dialog.show()
            }
        }
    }

    fun dialogWithIcon(context: Context, title: String, message: String, @DrawableRes icon: Int, okButton: String, cancelButton: String, onOkAction: () -> Unit, onCancelAction: () -> Unit) {
        synchronized(lock) {
            mAlertDialog.let {
                if (it != null && it.isShowing) {
                    it.dismiss()
                }
            }
            AlertDialog.Builder(context).also { dialog ->
                dialog.setCancelable(false)
                dialog.setTitle(title)
                dialog.setMessage(message)
                dialog.setIcon(icon)
                dialog.setPositiveButton(okButton) { dialogInterface, _ ->
                    onOkAction.invoke()
                    dialogInterface.dismiss()
                }
                dialog.setNegativeButton(cancelButton) { dialogInterface, _ ->
                    onCancelAction.invoke()
                    dialogInterface.dismiss()
                }
                mAlertDialog = dialog.show()
            }
        }
    }



}