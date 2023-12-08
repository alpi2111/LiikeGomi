package com.liike.liikegomi.payment.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.liike.liikegomi.add_address.ui.AddAddressActivity
import com.liike.liikegomi.background.utils.ActivityUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityPaymentBinding
import com.liike.liikegomi.payment.view_model.PaymentViewModel
import com.liike.liikegomi.payment.view_model.PaymentViewModelFactory

class PaymentActivity: BaseActivity<ActivityPaymentBinding, PaymentViewModel>() {

    private val addressLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK || result.data == null) {
            Log.e("NO_DATA", "NO_DATA_SELECTED_OR_CANCELED")
            return@registerForActivityResult
        }
        val address = result.data!!.getStringExtra(SELECTED_ADDRESS_KEY).toString()
        Log.d("DATA", address)
        mBinding.address.text = address
    }

    companion object {
        const val SELECTED_ADDRESS_KEY = "selectedAddressKey"
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
        AddAddressActivity.launchForResult(this, addressLauncher)
    }
}