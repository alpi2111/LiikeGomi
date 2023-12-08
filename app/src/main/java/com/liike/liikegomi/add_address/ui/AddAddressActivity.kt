package com.liike.liikegomi.add_address.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.add_address.view_model.AddAddressViewModel
import com.liike.liikegomi.add_address.view_model.AddAddressViewModelFactory
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityAddAddressBinding
import com.liike.liikegomi.payment.ui.PaymentActivity

class AddAddressActivity: BaseActivity<ActivityAddAddressBinding, AddAddressViewModel>() {

    companion object {
        fun launchForResult(appCompatActivity: AppCompatActivity, launcher: ActivityResultLauncher<Intent>) {
            val intent = Intent(appCompatActivity, AddAddressActivity::class.java)
            launcher.launch(intent)
        }
    }

    override val mViewModel: AddAddressViewModel
        get() = getViewModelFactory(this, AddAddressViewModelFactory())

    override fun inflate(): ActivityAddAddressBinding {
        return ActivityAddAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.run {
            btnCreate.setOnClickListener {
                // TODO: CALL VIEW MODEL FOR ADDING ADDRESS
                val intent = Intent().apply {
                    putExtra(PaymentActivity.SELECTED_ADDRESS_KEY, "ADDRESS ADDED")
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}