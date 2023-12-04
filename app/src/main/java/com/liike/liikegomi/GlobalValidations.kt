package com.liike.liikegomi

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.liike.liikegomi.background.utils.TextUtils

fun TextInputEditText.isValid(parent: TextInputLayout, errorMessage: String = "Campo requerido"): Boolean {
    this.addTextChangedListener(TextUtils.removeInputLayoutError(parent))
    if (this.text.isNullOrBlank()) {
        parent.error = errorMessage
        return false
    }
    parent.error = null
    return true
}

fun TextInputEditText.text(): String {
    return text?.toString()?.trim() ?: throw RuntimeException("The text you are requesting is not validated, please consider using isValid(parent, errorMessage) before trying to use its value")
}


