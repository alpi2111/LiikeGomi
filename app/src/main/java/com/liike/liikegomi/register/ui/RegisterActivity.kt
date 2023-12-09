package com.liike.liikegomi.register.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch

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
            lifecycleScope.launch {
                val privacyTerms = mViewModel.getPrivacyTerms()
                MessageUtils.dialog(
                    context = this@RegisterActivity,
                    title = "Aviso de privacidad",
                    message = privacyTerms,
                    okButton = "Aceptar",
                    cancelButton = "Rechazar",
                    onOkAction = {
                        val user = Usuarios()
                        mBinding.run {
                            user.name = etName.text()
                            user.lastName = etLastName.text()
                            user.password = etPassword.text()
                            user.email = etEmail.text()
                            user.userName = etUserName.text()
                            user.birthDay = DateUtils.getCurrentTimestampFormatted()
                            user.phoneNumber = etNumber.text()
                        }
                        mViewModel.saveUser(this@RegisterActivity, user)
                    },
                    onCancelAction = {
                        MessageUtils.toast(this@RegisterActivity, "Rechazaste el aviso de privacidad, no creamos la cuenta", Toast.LENGTH_LONG)
                    }
                )
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
            return etName.isValid(ilName) and
                    etLastName.isValid(ilLastName) and
                    etUserName.isValid(ilUserName) and
                    etEmail.isValid(ilEmail) and
                    etBirthDate.isValid(ilBirthDate) and
                    etNumber.isValid(ilNumber) and
                    etPassword.isValid(ilPassword) and
                    etPasswordRepeated.isValid(ilPasswordRepeated)
        }
    }
}