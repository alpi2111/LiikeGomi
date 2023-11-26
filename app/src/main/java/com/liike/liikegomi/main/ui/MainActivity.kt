package com.liike.liikegomi.main.ui

import android.os.Bundle
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityMainBinding
import com.liike.liikegomi.login.view_model.LoginViewModel
import com.liike.liikegomi.login.view_model.LoginViewModelFactory

class MainActivity : BaseActivity<ActivityMainBinding, LoginViewModel>() {
    override val mViewModel: LoginViewModel
        get() = getViewModelFactory(this, LoginViewModelFactory())

    override fun inflate(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.template()
    }
}