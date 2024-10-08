package com.liike.liikegomi.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.background.utils.TextUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityLoginBinding
import com.liike.liikegomi.isValid
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

        mBinding.etUser.editableText.filters = TextUtils.noSpacesFilter()
        mBinding.etPassword.filters = TextUtils.noSpacesFilter()

        mBinding.btnLogin.setOnClickListener {
            if (isFormValid())
                mViewModel.login(this, mBinding.etUser.text!!.toString(), mBinding.etPassword.text!!.toString())
            else
                MessageUtils.toast(this, "Faltan algunos campos")
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

    private fun isFormValid(): Boolean {
        mBinding.run {
            return etUser.isValid(ilUser, "El usuario no puede estar vacío") and
                    etPassword.isValid(ilPassword, "La contraseña no puede estar vacía")
        }
    }
}