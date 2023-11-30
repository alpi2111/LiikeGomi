package com.liike.liikegomi.background.utils

import android.text.InputFilter

object TextUtils {

    private val spacesRegex by lazy { "\\s*".toRegex() }

    fun noSpacesFilter(): Array<InputFilter> {
        val filter = InputFilter { source, _, _, _, _, _ ->
            return@InputFilter source.replace(spacesRegex, "")
        }
        return arrayOf(filter)
    }
}