package com.liike.liikegomi.register.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.liike.liikegomi.background.firebase_db.entities.Usuarios
import com.liike.liikegomi.background.utils.DateUtils
import com.liike.liikegomi.background.utils.MessageUtils
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

        mViewModel.mWasRegistered.observe(this) {
            if (it)
                finish()
        }

        mBinding.btnCreate.setOnClickListener {
            if (!allFieldsAreFilled()) {
                MessageUtils.toast(this, "Faltan algunos campos!")
                return@setOnClickListener
            }
            if (mBinding.etPassword.text() != mBinding.etPasswordRepeated.text()) {
                MessageUtils.toast(this, "Las contraseñas no coinciden")
                return@setOnClickListener
            }
            mBinding.run {
                val user = Usuarios(
                    name = etName.text(),
                    lastName = etLastName.text(),
                    password = etPassword.text(),
                    email = etEmail.text(),
                    userName = etUserName.text(),
                    birthDay = DateUtils.getCurrentTimestampFormatted(),
                )
                mViewModel.saveUser(this@RegisterActivity, user)
            }
        }
    }

    private fun allFieldsAreFilled(): Boolean {
        mBinding.run {
            return etName.isFill() and etLastName.isFill() and etUserName.isFill() and
                    etEmail.isFill() and etBirthDate.isFill() and etNumber.isFill() and
                    etPassword.isFill() and etPasswordRepeated.isFill()
        }
    }

    private fun TextInputEditText.isFill(): Boolean {
        return !this.text.isNullOrBlank()
    }

    private fun TextInputEditText.text(): String {
        return this.text!!.toString()
    }
}