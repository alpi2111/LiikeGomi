package com.liike.liikegomi.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
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
                SharedPrefs.bool(SharedPreferenceKeys.IS_USER_LOGGED, true)
                MainActivity.launch(this)
            }
        }

        mBinding.btnLogin.setOnClickListener {
            mViewModel.login(this, mBinding.etUser.text!!.toString(), mBinding.etPassword.text!!.toString())
//            lifecycleScope.launch {
//                var rol = Rol(RolType.USER)
//                Dao.getInstance().rolDao().insert(rol)
//                rol = Rol(RolType.ADMIN)
//                Dao.getInstance().rolDao().insert(rol)
//            }
        }

        mBinding.btnRegister.setOnClickListener {
            RegisterActivity.launch(this)
        }

        mBinding.forgotPassword.setOnClickListener {
            MessageUtils.toast(this, "Olvidé contraseña")
        }
    }
}