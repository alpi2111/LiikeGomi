package com.liike.liikegomi.login.ui

import android.os.Bundle
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityLoginBinding
import com.liike.liikegomi.login.view_model.LoginViewModel
import com.liike.liikegomi.login.view_model.LoginViewModelFactory

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override val mViewModel: LoginViewModel
        get() = getViewModelFactory(this, LoginViewModelFactory())

    override fun inflate(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.template()
        mBinding.btnLogin.setOnClickListener {
            MessageUtils.toast(this, "Mensaje")
        }
        mBinding.forgotPassword.setOnClickListener {
            MessageUtils.toast(this, "Olvidé contraseña")
        }
    }
}