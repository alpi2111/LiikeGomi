package com.liike.liikegomi.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.liike.liikegomi.background.database.Dao
import com.liike.liikegomi.background.database.entities.Direcciones
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityLoginBinding
import com.liike.liikegomi.login.view_model.LoginViewModel
import com.liike.liikegomi.login.view_model.LoginViewModelFactory
import kotlinx.coroutines.launch

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
        mViewModel.template()
        mBinding.btnLogin.setOnClickListener {
            MessageUtils.toast(this, "Mensaje")
            val direction = Direcciones(
                direccion = "inciderint",
                estado = "accumsan",
                municipio = "movet",
                colonia = "ancillae",
                calle = "eirmod",
                cp = "singulis",
                numExterior = "ligula",
                numInterior = "dicunt",
                telefono = "risus",
                referencias = "aeque"
            )
            lifecycleScope.launch {
                Dao.saveDirection(direction)
            }
        }
        mBinding.forgotPassword.setOnClickListener {
            MessageUtils.toast(this, "Olvidé contraseña")
        }
    }
}