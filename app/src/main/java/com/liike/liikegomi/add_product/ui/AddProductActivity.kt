package com.liike.liikegomi.add_product.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.liike.liikegomi.add_product.view_model.AddProductViewModel
import com.liike.liikegomi.add_product.view_model.AddProductViewModelFactory
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityAddProductBinding
import com.liike.liikegomi.image_picker.background.interfaces.ImageSelectionCallback
import com.liike.liikegomi.image_picker.ui.SelectImageBottomSheet

class AddProductActivity : BaseActivity<ActivityAddProductBinding, AddProductViewModel>() {

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
                    Log.i("onImageSelected", byteArray.toString())
                }
            })
        }
    }
}