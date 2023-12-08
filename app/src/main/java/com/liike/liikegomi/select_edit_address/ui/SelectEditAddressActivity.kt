package com.liike.liikegomi.select_edit_address.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.add_address.ui.AddAddressActivity
import com.liike.liikegomi.background.firebase_db.entities.Direcciones
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivitySelectEditAddressBinding
import com.liike.liikegomi.payment.ui.PaymentActivity
import com.liike.liikegomi.select_edit_address.adapter.ItemAddressSelectionAdapter
import com.liike.liikegomi.select_edit_address.view_model.SelectEditAddressViewModel
import com.liike.liikegomi.select_edit_address.view_model.SelectEditAddressViewModelFactory

class SelectEditAddressActivity: BaseActivity<ActivitySelectEditAddressBinding, SelectEditAddressViewModel>() {

    private lateinit var mAddressAdapter: ItemAddressSelectionAdapter
    private val mAddAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != RESULT_OK || result.data == null) {
            MessageUtils.toast(this, "No agregaste ninguna dirección, selecciona alguna de la lista")
            return@registerForActivityResult
        }
        val address = result.data!!.getSerializableExtra(PaymentActivity.SELECTED_ADDRESS_KEY) as Direcciones
        mAddressAdapter.addAndSelectLastAddress(address)
    }

    companion object {
        private const val ADDRESS_LIST_KEY = "addressListKey"
        fun launchForResult(appCompatActivity: AppCompatActivity, launcher: ActivityResultLauncher<Intent>, addressList: List<Direcciones>) {
            val intent = Intent(appCompatActivity, SelectEditAddressActivity::class.java).apply {
                putExtra(ADDRESS_LIST_KEY, addressList.toTypedArray())
            }
            launcher.launch(intent)
        }
    }

    override val mViewModel: SelectEditAddressViewModel
        get() = getViewModelFactory(this, SelectEditAddressViewModelFactory())

    override fun inflate(): ActivitySelectEditAddressBinding {
        return ActivitySelectEditAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sendAddressData()
            }
        })

        val addressList = (intent.getSerializableExtra(ADDRESS_LIST_KEY) as Array<Direcciones>).toList()

        mAddressAdapter = ItemAddressSelectionAdapter()
        mBinding.recyclerAddress.adapter = mAddressAdapter
        mAddressAdapter.setData(addressList)

        mBinding.btnSelect.setOnClickListener { sendAddressData() }
        mBinding.btnAddAddress.setOnClickListener {
            AddAddressActivity.launchForResult(this, mAddAddressLauncher)
        }
    }

    private fun sendAddressData() {
        val selectedAddress = mAddressAdapter.getSelectedAddress()
        val intent = Intent().apply {
            putExtra(PaymentActivity.SELECTED_ADDRESS_KEY, selectedAddress)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}