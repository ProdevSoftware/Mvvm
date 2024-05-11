package com.example.Mvvm.helper

import android.icu.text.DateFormatSymbols
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import timber.log.Timber
import java.util.*

const val CLOCK_IN_OUT_FORMAT = "MM/dd/yy hh:mm a"

fun String.toDate(
    dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date? {
    try {
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        //parser.timeZone = TimeZone.getDefault()
        return parser.parse(this)
    } catch(e: Exception) {
        Timber.e(e, "Invalid Format Time :'$this' ")
    }
    return null

}

fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}

fun Int.getMonthNameFromInt(): String {
    return DateFormatSymbols.getInstance().months[this]
}