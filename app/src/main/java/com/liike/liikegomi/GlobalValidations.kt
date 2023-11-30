package com.liike.liikegomi

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.liike.liikegomi.background.utils.TextUtils

fun TextInputEditText.isValid(parent: TextInputLayout, errorMessage: String): Boolean {
    this.addTextChangedListener(TextUtils.removeInputLayoutError(parent))
    if (this.text.isNullOrBlank()) {
        parent.error = errorMessage
        return false
    }
    parent.error = null
    return true
}

