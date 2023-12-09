package com.liike.liikegomi.payment.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.liike.liikegomi.R
import com.liike.liikegomi.add_address.ui.AddAddressActivity
import com.liike.liikegomi.background.firebase_db.base.PaymentType
import com.liike.liikegomi.background.firebase_db.entities.Carrito
import com.liike.liikegomi.background.firebase_db.entities.Direcciones
import com.liike.liikegomi.background.utils.ActivityUtils
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityPaymentBinding
import com.liike.liikegomi.main.ui.MainActivity
import com.liike.liikegomi.payment.view_model.PaymentViewModel
import com.liike.liikegomi.payment.view_model.PaymentViewModelFactory
import com.liike.liikegomi.select_edit_address.ui.SelectEditAddressActivity
import com.liike.liikegomi.shopping_cart.ui.ShoppingCartActivity

class PaymentActivity : BaseActivity<ActivityPaymentBinding, PaymentViewModel>() {

    private var mAddressList: List<Direcciones> = listOf()
    private var mAddressSelected: Direcciones? = null
    private var mPaymentType: PaymentType = PaymentType.TRANSFER

    private val addressLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK || result.data == null) {
            Log.e("NO_DATA", "NO_DATA_SELECTED_OR_CANCELED")
            @SuppressLint("SetTextI18n")
            mBinding.address.text = "SIN DIRECCIÓN"
            mBinding.btnCreate.isEnabled = false
            return@registerForActivityResult
        }
        mAddressSelected = result.data!!.getSerializableExtra(SELECTED_ADDRESS_KEY) as Direcciones
        Log.d("DATA", mAddressSelected!!.formattedAddress())
        mBinding.address.text = mAddressSelected!!.formattedAddress()
        mBinding.btnCreate.isEnabled = true
    }

    companion object {
        const val SELECTED_ADDRESS_KEY = "selectedAddressKey"
        fun launch(appCompatActivity: AppCompatActivity, cartId: String, cartProductsList: List<Carrito.Producto>) {
            val intent = Intent(appCompatActivity, PaymentActivity::class.java).apply {
                putExtra(ShoppingCartActivity.SHOPPING_CART_ID_KEY, cartId)
                putExtra(ShoppingCartActivity.SHOPPING_CART_KEY, cartProductsList.toTypedArray())
            }
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

        val cartList = (intent.getSerializableExtra(ShoppingCartActivity.SHOPPING_CART_KEY) as Array<Carrito.Producto>).toList()
        val cartId = intent.getStringExtra(ShoppingCartActivity.SHOPPING_CART_ID_KEY)!!

        mViewModel.mUserAddresses.observe(this) { addresses ->
            if (addresses.isEmpty()) {
                AddAddressActivity.launchForResult(this, addressLauncher)
                return@observe
            }
            mBinding.address.text = addresses.first().formattedAddress()
            mAddressList = addresses
            mAddressSelected = addresses.first()
        }

        mViewModel.mSellWasSuccess.observe(this) {
            if (it) {
                MessageUtils.dialogWithIcon(
                    context = this@PaymentActivity,
                    title = "Exitoso",
                    message = "Tu compra ha sido realizada con éxito 😃",
                    icon = R.drawable.ic_check_circle,
                    okButton = "Ok",
                    onAction = {
                        MainActivity.launch(this)
                    }
                )
            } else {
                MessageUtils.dialogWithIcon(
                    context = this@PaymentActivity,
                    title = "Error",
                    message = "Tu compra no se puedo realizar 😔 ¿Qué deseas hacer?",
                    icon = R.drawable.ic_cancel,
                    okButton = "Reintentar",
                    cancelButton = "Regresar al inicio",
                    onOkAction = {
                        mViewModel.createSell(cartId, cartList, mAddressSelected!!.formattedAddress(), mPaymentType)
                    },
                    onCancelAction = {
                        MainActivity.launch(this)
                    }
                )
            }
        }

        mBinding.run {
            radioGroupPayment.setOnCheckedChangeListener { _, checkedId ->
                val shouldShowTransferText = checkedId == radioButtonTransfer.id
                mPaymentType = if (shouldShowTransferText)
                    PaymentType.TRANSFER
                else
                    PaymentType.CASH
                transferText.isVisible = shouldShowTransferText
                lyCopyButtons.isVisible = shouldShowTransferText
            }

            btnAddSelectAddress.setOnClickListener {
                if (mAddressList.isEmpty())
                    AddAddressActivity.launchForResult(this@PaymentActivity, addressLauncher)
                else
                    SelectEditAddressActivity.launchForResult(this@PaymentActivity, addressLauncher, mAddressList)
            }

            btnCopyClabe.setOnClickListener {
                ActivityUtils.copyToClipboard(this@PaymentActivity, "CLABE", "127899013836170214")
            }

            btnCopyNumber.setOnClickListener {
                ActivityUtils.copyToClipboard(this@PaymentActivity, "Telefono", "+527831280145")
            }

            btnCreate.setOnClickListener {
                mViewModel.createSell(cartId, cartList, mAddressSelected!!.formattedAddress(), mPaymentType)
            }
        }

        mViewModel.getUserAddresses()
    }
}