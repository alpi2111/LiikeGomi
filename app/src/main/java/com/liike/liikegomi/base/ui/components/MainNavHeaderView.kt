package com.liike.liikegomi.base.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.background.utils.CryptUtils
import com.liike.liikegomi.databinding.NavHeaderMainBinding

class MainNavHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {
    private val mBinding: NavHeaderMainBinding
    init {
        mBinding = NavHeaderMainBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater, this, true)
        mBinding.run {
            val name = SharedPrefs.string(SharedPreferenceKeys.NAME_OF_USER)
            val lastName = SharedPrefs.string(SharedPreferenceKeys.LAST_NAME_USER)
            val userEmail = SharedPrefs.string(SharedPreferenceKeys.EMAIL_USER)
            val phoneNumber = SharedPrefs.string(SharedPreferenceKeys.PHONE_USER)

            @SuppressLint("SetTextI18n")
            fullName.text = "$name $lastName"
            email.text = if (userEmail.isNullOrBlank()) "null" else CryptUtils.decrypt(userEmail)
            phone.text = if (phoneNumber.isNullOrBlank()) "null" else phoneNumber
        }
    }
}