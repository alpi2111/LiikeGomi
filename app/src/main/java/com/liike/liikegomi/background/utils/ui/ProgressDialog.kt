package com.liike.liikegomi.background.utils.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.liike.liikegomi.base.ui.BaseDialog
import com.liike.liikegomi.databinding.DialogProgressBinding

class ProgressDialog private constructor(private val message: String): BaseDialog<DialogProgressBinding>() {

    companion object {
        val TAG: String = this::class.java.simpleName
        fun show(fragmentManager: FragmentManager, message: String) {
            ProgressDialog(message).apply {
                show(fragmentManager, TAG)
            }
        }
    }

    override fun inflate(): DialogProgressBinding {
        return DialogProgressBinding.inflate(layoutInflater)
    }

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        mBinding.progressText.text = message
    }
}