package com.liike.liikegomi.background.utils

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
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

    fun onlyNumbersFilter(): Array<InputFilter> {
        val filter = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (Character.isDigit(source[i])) {
                    return@InputFilter source[i].toString()
                }
            }
            ""
        }
        return arrayOf(filter)
    }

}