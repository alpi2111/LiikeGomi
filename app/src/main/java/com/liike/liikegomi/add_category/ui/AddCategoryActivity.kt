package com.liike.liikegomi.add_category.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.liike.liikegomi.add_category.view_model.AddCategoryViewModel
import com.liike.liikegomi.add_category.view_model.AddCategoryViewModelFactory
import com.liike.liikegomi.background.firebase_db.entities.Categoria
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityAddCategoryBinding
import com.liike.liikegomi.isValid
import com.liike.liikegomi.text

class AddCategoryActivity : BaseActivity<ActivityAddCategoryBinding, AddCategoryViewModel>() {

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, AddCategoryActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: AddCategoryViewModel
        get() = ViewModelProvider(this, AddCategoryViewModelFactory())[AddCategoryViewModel::class.java]

    override fun inflate(): ActivityAddCategoryBinding {
        return ActivityAddCategoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.mCategoryAdded.observe(this) {
            // TODO: DO SOMETHING
        }

        mBinding.btnCreate.setOnClickListener {
            if (!areFieldsValid()) {
                MessageUtils.toast(this, "Faltan algunos campos")
                return@setOnClickListener
            }

            val category = Categoria(
                category = mBinding.etCategoryName.text(),
                isVisible = mBinding.checkboxIsVisible.isChecked,
                idCategory = -1
            )
            mViewModel.getCategoryIdAndThenAdd(category)
        }
    }

    private fun areFieldsValid(): Boolean {
        return mBinding.run {
            etCategoryName.isValid(ilCategoryName)
        }
    }
}