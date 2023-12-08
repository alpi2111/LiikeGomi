package com.liike.liikegomi.add_address.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.add_address.view_model.AddAddressViewModel
import com.liike.liikegomi.add_address.view_model.AddAddressViewModelFactory
import com.liike.liikegomi.background.firebase_db.entities.Direcciones
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityAddAddressBinding
import com.liike.liikegomi.isValid
import com.liike.liikegomi.payment.ui.PaymentActivity
import com.liike.liikegomi.text

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

        mViewModel.mAddressAdded.observe(this) {
            if (it.isNotEmpty()) {
                val intent = Intent().apply {
                    putExtra(PaymentActivity.SELECTED_ADDRESS_KEY, it)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        mBinding.run {
            btnCreate.setOnClickListener {
                if (!validInfo()) {
                    MessageUtils.toast(this@AddAddressActivity, "Faltan algunos campos")
                    return@setOnClickListener
                }
                val address = Direcciones(
                    calle = etStreet.text(),
                    numExterior = etExteriorNumber.text(),
                    numInterior = etInteriorNumber.text(),
                    colonia = etColony.text(),
                    cp = etCp.text(),
                    municipio = etTown.text(),
                    estado = etState.text(),
                    telefono = etPhone.text(),
                    referencias = etReferences.text(),
                )
                mViewModel.addAddress(address)
            }
        }
    }

    private fun validInfo(): Boolean {
        mBinding.run {
            return etStreet.isValid(ilStreet) and
                    etExteriorNumber.isValid(ilExteriorNumber) and
                    etInteriorNumber.isValid(ilInteriorNumber) and
                    etColony.isValid(ilColony) and
                    etCp.isValid(ilCp) and
                    etTown.isValid(ilTown) and
                    etState.isValid(ilState) and
                    etPhone.isValid(ilPhone) and
                    etReferences.isValid(ilReferences)
        }
    }
}