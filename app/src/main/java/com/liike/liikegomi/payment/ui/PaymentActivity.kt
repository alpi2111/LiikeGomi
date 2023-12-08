package com.liike.liikegomi.payment.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.liike.liikegomi.background.utils.ActivityUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityPaymentBinding
import com.liike.liikegomi.payment.view_model.PaymentViewModel
import com.liike.liikegomi.payment.view_model.PaymentViewModelFactory

class PaymentActivity: BaseActivity<ActivityPaymentBinding, PaymentViewModel>() {

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, PaymentActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: PaymentViewModel
        get() = getViewModelFactory(this, PaymentViewModelFactory())

    override fun inflate(): ActivityPaymentBinding {
        return ActivityPaymentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.run {
            radioGroupPayment.setOnCheckedChangeListener { _, checkedId ->
                val shouldShowTransferText = checkedId == radioButtonTransfer.id
                transferText.isVisible = shouldShowTransferText
                lyCopyButtons.isVisible = shouldShowTransferText
            }

            btnCopyClabe.setOnClickListener {
                ActivityUtils.copyToClipboard(this@PaymentActivity, "CLABE", "127899013836170214")
            }

            btnCopyNumber.setOnClickListener {
                ActivityUtils.copyToClipboard(this@PaymentActivity, "Telefono", "+527831280145")
            }
        }
    }
}