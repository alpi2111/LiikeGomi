package com.liike.liikegomi.register.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityRegisterBinding
import com.liike.liikegomi.register.view_model.RegisterViewModel
import com.liike.liikegomi.register.view_model.RegisterViewModelFactory

class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterViewModel>() {

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, RegisterActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: RegisterViewModel
        get() = getViewModelFactory(this, RegisterViewModelFactory())

    override fun inflate(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}