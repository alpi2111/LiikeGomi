package com.liike.liikegomi.background.utils

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import com.google.android.material.textfield.TextInputLayout

object TextUtils {

    private val spacesRegex by lazy { "\\s*".toRegex() }

    fun noSpacesFilter(): Array<InputFilter> {
        val filter = InputFilter { source, _, _, _, _, _ ->
            return@InputFilter source.replace(spacesRegex, "")
        }
        return arrayOf(filter)
    }

    fun removeInputLayoutError(layout: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (!p0.isNullOrBlank()) {
                    layout.error = null
                }
            }
        }
    }

}