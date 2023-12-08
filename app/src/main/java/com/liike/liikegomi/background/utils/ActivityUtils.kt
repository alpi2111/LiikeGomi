package com.liike.liikegomi.background.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.text.InputType
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

object ActivityUtils {

    private var mIsDatePickerDialogOpen = false

    fun hideKeyboard(activity: AppCompatActivity) {
        val inputMethodManager = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(activity.window.decorView.rootView.windowToken, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun configureEditTextForDatePicker(activity: AppCompatActivity, view: TextInputEditText, minDate: Long? = null, maxDate: Long? = null) {
        view.run {
            inputType = InputType.TYPE_NULL
            keyListener = null
            setOnTouchListener { _, motionEvent ->
                hideKeyboard(activity)
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    if (!mIsDatePickerDialogOpen)
                        showDatePickerDialogWhenTouchEditText(activity, this, minDate, maxDate)
                }
                false
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialogWhenTouchEditText(context: Context, editText: TextInputEditText, minDate: Long? = null, maxDate: Long? = null) {
        mIsDatePickerDialogOpen = true
        fun Int.addZeroToStart(): String {
            return this.toString().padStart(2, '0')
        }

        fun getCalendar(type: Int): Int {
            val instance = Calendar.getInstance()
            return instance.get(type)
        }


        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            editText.setText("${day.addZeroToStart()}/${(month + 1).addZeroToStart()}/$year")
        }

        var year: Int
        var month: Int
        var day: Int

        editText.text.let {
            if (it.isNullOrBlank()) {
                year = getCalendar(Calendar.YEAR)
                month = getCalendar(Calendar.MONTH)
                day = getCalendar(Calendar.DAY_OF_MONTH)
            } else {
                val split = it.split("/")
                year = split[2].toInt()
                month = split[1].toInt() - 1
                day = split[0].toInt()
            }
        }

        val dialog = DatePickerDialog(context, dateListener, year, month, day)
        if (minDate != null)
            dialog.datePicker.minDate = minDate
        if (maxDate != null)
            dialog.datePicker.maxDate = maxDate
        dialog.setOnDismissListener { mIsDatePickerDialogOpen = false }
        dialog.setCancelable(false)
        dialog.show()
    }

    fun resetDatePickerDialogVisibility() {
        mIsDatePickerDialogOpen = false
    }

    fun copyToClipboard(activity: AppCompatActivity, label: String, text: String) {
        val clipboard = activity.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        MessageUtils.toast(activity, "$label copiado al portapapeles")
    }
}