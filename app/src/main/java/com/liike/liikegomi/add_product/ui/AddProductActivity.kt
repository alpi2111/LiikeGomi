package com.liike.liikegomi.add_product.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.Blob
import com.liike.liikegomi.add_product.view_model.AddProductViewModel
import com.liike.liikegomi.add_product.view_model.AddProductViewModelFactory
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityAddProductBinding
import com.liike.liikegomi.image_picker.background.interfaces.ImageSelectionCallback
import com.liike.liikegomi.image_picker.ui.SelectImageBottomSheet
import com.liike.liikegomi.text

class AddProductActivity : BaseActivity<ActivityAddProductBinding, AddProductViewModel>() {

    private var mImage: ByteArray? = null

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, AddProductActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: AddProductViewModel
        get() = ViewModelProvider(this, AddProductViewModelFactory())[AddProductViewModel::class.java]

    override fun inflate(): ActivityAddProductBinding {
        return ActivityAddProductBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.btnTakePhoto.setOnClickListener {
            SelectImageBottomSheet.show(supportFragmentManager, object : ImageSelectionCallback {
                override fun onImageSelected(byteArray: ByteArray?) {
                    if (byteArray != null) {
                        MessageUtils.toast(this@AddProductActivity, "Image selected")
                        mImage = byteArray
                    }
                }
            })
        }
        mBinding.btnCreate.setOnClickListener {
            /*
            var productName: String,
    @get:PropertyName("descripcion")
    @set:PropertyName("descripcion")
    var productDescription: String,
    @get:PropertyName("precio_producto")
    @set:PropertyName("precio_producto")
    var productPrice: Double,
    @get:PropertyName("stocks")
    @set:PropertyName("stocks")
    var productStock: Int,
    @get:PropertyName("id_cat")
    @set:PropertyName("id_cat")
    var idCategoria: Int,
    @get:PropertyName("id_producto")
    @set:PropertyName("id_producto")
    var productId: String,
    @get:PropertyName("visible")
    @set:PropertyName("visible")
    var isVisible: Boolean = true,
    @get:PropertyName("imagen")
    @set:PropertyName("imagen")
    var productImage: ByteArray? = null,
             */
            val product = Productos()
            mBinding.run {
                product.productName = etProductName.text()
                product.idCategoria = 1
                product.isVisible = true
                product.productDescription = etProductDescription.text()
                product.productId = "1"
                product.productStock = etStock.text().toInt()
                product.productPrice = etPrice.text().toDouble()
                product.productImage = Blob.fromBytes(mImage!!)
            }
            mViewModel.addProduct(product)
        }
    }
}