package com.liike.liikegomi.login.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.background.utils.CryptUtils
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityLoginBinding
import com.liike.liikegomi.login.view_model.LoginViewModel
import com.liike.liikegomi.login.view_model.LoginViewModelFactory
import com.liike.liikegomi.main.ui.MainActivity
import com.liike.liikegomi.register.ui.RegisterActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, LoginActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: LoginViewModel
        get() = getViewModelFactory(this, LoginViewModelFactory())

    override fun inflate(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.mWasLogged.observe(this) {
            if (it) {
                MainActivity.launch(this)
            }
        }

        mBinding.btnLogin.setOnClickListener {
            val toEncrypt = "HolaMundo@gmil.com"
            val encrypted = CryptUtils.encrypt(toEncrypt)
            Log.d("encrypted", encrypted)
            Log.d("decrypted", CryptUtils.decrypt(encrypted))
        }

        mBinding.btnRegister.setOnClickListener {
            RegisterActivity.launch(this)
        }

        mBinding.forgotPassword.setOnClickListener {
            MessageUtils.toast(this, "Olvidé contraseña")
        }
    }
}