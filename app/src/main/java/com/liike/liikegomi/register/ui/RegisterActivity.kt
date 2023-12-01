package com.liike.liikegomi.register.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.liike.liikegomi.background.firebase_db.entities.Usuarios
import com.liike.liikegomi.background.utils.ActivityUtils
import com.liike.liikegomi.background.utils.DateUtils
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.background.utils.TextUtils
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityRegisterBinding
import com.liike.liikegomi.isValid
import com.liike.liikegomi.register.view_model.RegisterViewModel
import com.liike.liikegomi.register.view_model.RegisterViewModelFactory
import com.liike.liikegomi.text

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

        mBinding.run {
            etUserName.filters = TextUtils.noSpacesFilter()
            etEmail.filters = TextUtils.noSpacesFilter()
            etBirthDate.filters = TextUtils.noSpacesFilter()
            etNumber.filters = TextUtils.noSpacesFilter() + TextUtils.onlyNumbersFilter().plus(InputFilter.LengthFilter(10))
            etPassword.filters = TextUtils.noSpacesFilter()
            etPasswordRepeated.filters = TextUtils.noSpacesFilter()

            etEmail.doOnTextChanged { text, _, _, _ ->
                if (TextUtils.isEmailValid(text?.toString() ?: ""))
                    ilEmail.error = null
                else
                    ilEmail.error = "Correo inválido"
            }

            etPassword.doOnTextChanged { text, _, _, _ ->
                if (TextUtils.isPasswordValid(text?.toString() ?: ""))
                    ilPassword.error = null
                else
                    ilPassword.error = "Contraseña inválida"
            }

            etPasswordRepeated.doOnTextChanged { text, _, _, _ ->
                if (etPassword.text() != (text?.toString() ?: ""))
                    ilPasswordRepeated.error = "Las contraseñas no coinciden"
                else
                    ilPasswordRepeated.error = null
            }

            ActivityUtils.configureEditTextForDatePicker(this@RegisterActivity, etBirthDate, maxDate = DateUtils.getCurrentDate().time)
        }
    }

    override fun onDestroy() {
        ActivityUtils.resetDatePickerDialogVisibility()
        super.onDestroy()
    }

    private fun allFieldsAreFilled(): Boolean {
        mBinding.run {
            val errorMessage = "El campo es obligatorio"
            return etName.isValid(ilName, errorMessage) and
                    etLastName.isValid(ilLastName, errorMessage) and
                    etUserName.isValid(ilUserName, errorMessage) and
                    etEmail.isValid(ilEmail, errorMessage) and
                    etBirthDate.isValid(ilBirthDate, errorMessage) and
                    etNumber.isValid(ilNumber, errorMessage) and
                    etPassword.isValid(ilPassword, errorMessage) and
                    etPasswordRepeated.isValid(ilPasswordRepeated, errorMessage)
        }
    }
}