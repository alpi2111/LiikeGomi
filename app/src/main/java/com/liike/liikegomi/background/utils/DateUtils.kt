package com.liike.liikegomi.background.utils

import com.google.firebase.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val calendar: Calendar by lazy { Calendar.getInstance() }
    private val dateFormatter: DateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    fun getFormattedDateAsString(date: Date): String {
        return dateFormatter.format(date)
    }

    fun getFormattedDateFromDate(date: Date): Date {
        return dateFormatter.parse(dateFormatter.format(date))!!
    }

    fun getFormattedDateFromTimestamp(timestamp: Long): Date {
        return getFormattedDateFromDate(Date(timestamp))
    }

    fun getFormattedDateFromTimestamp(timestamp: Timestamp): Date {
        val date = timestamp.toDate()
        return getFormattedDateFromDate(date)
    }

    fun getFormattedDateFromString(str: String): Date {
        return dateFormatter.parse(str)!!
    }

    fun getCurrentDate(): Date {
        return dateFormatter.parse(dateFormatter.format(calendar.time))!!
    }

    fun getCurrentTimestampFormatted(): Timestamp {
        val time = Timestamp.now().toDate()
        val dateFormattedStr = dateFormatter.format(time)
        val dateFormatted = dateFormatter.parse(dateFormattedStr)
        return Timestamp(dateFormatted!!)
    }

    fun getCurrentTimestampWithoutFormat(): Timestamp {
        return Timestamp.now()
    }

    fun getCurrentDateWithoutFormat(): Date {
        return Timestamp.now().toDate()
    }

}