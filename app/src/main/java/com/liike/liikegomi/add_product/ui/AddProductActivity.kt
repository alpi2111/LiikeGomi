package com.liike.liikegomi.add_product.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.firestore.Blob
import com.liike.liikegomi.R
import com.liike.liikegomi.add_product.adapters.SpinnerAdapter
import com.liike.liikegomi.add_product.view_model.AddProductViewModel
import com.liike.liikegomi.add_product.view_model.AddProductViewModelFactory
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.background.firebase_db.entities.Productos
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityAddProductBinding
import com.liike.liikegomi.image_picker.background.interfaces.ImageSelectionCallback
import com.liike.liikegomi.image_picker.ui.SelectImageBottomSheet
import com.liike.liikegomi.text

class AddProductActivity : BaseActivity<ActivityAddProductBinding, AddProductViewModel>() {

    private var mImage: ByteArray? = null
    private var defaultImagePadding: Int = 0

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
        mViewModel.mCategories.observe(this) { categories ->
            if (categories == null) {
                // TODO: Maybe call the add category activity or something
                return@observe
            }
            val category = Categoria("Elegir categoría", true,0)
            mBinding.spinnerCategory.adapter = SpinnerAdapter(this@AddProductActivity, category, categories)
        }

        mViewModel.mProductAdded.observe(this) {
            Log.i("aaa", "aaaaaaa $it")
        }

        mBinding.btnTakePhoto.setOnClickListener {
            SelectImageBottomSheet.show(supportFragmentManager, object : ImageSelectionCallback {
                override fun onImageSelected(byteArray: ByteArray?) {
                    if (byteArray != null) {
                        mImage = byteArray
                        mBinding.btnTakePhoto.setPadding(0, 0, 0, 0)
                        mBinding.btnTakePhoto.imageTintList = null
                        Glide.with(this@AddProductActivity).load(byteArray).centerCrop().into(mBinding.btnTakePhoto)
                    } else {
                        mBinding.btnTakePhoto.setPadding(0, defaultImagePadding, 0, defaultImagePadding)
                        mBinding.btnTakePhoto.imageTintList = ContextCompat.getColorStateList(this@AddProductActivity, R.color.red_light)
                        mBinding.btnTakePhoto.background = AppCompatResources.getDrawable(this@AddProductActivity, R.drawable.bg_photo_taker_red)
                        Glide.with(this@AddProductActivity).load(R.drawable.ic_add_a_photo).into(mBinding.btnTakePhoto)
                    }
                }
            })
        }
        mBinding.btnCreate.setOnClickListener {
            val product = Productos()
            mBinding.run {
                product.productName = etProductName.text()
                product.idCategoria = 1
                product.isVisible = true
                product.productDescription = etProductDescription.text()
                product.productStock = etStock.text().toInt()
                product.productPrice = etPrice.text().toDouble()
                product.productImage = Blob.fromBytes(mImage!!)
            }
            mViewModel.getProductIdAndThenAdd(product)
        }

        mViewModel.getCategories()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        defaultImagePadding = mBinding.btnTakePhoto.paddingTop
        super.onPostCreate(savedInstanceState)
    }
}