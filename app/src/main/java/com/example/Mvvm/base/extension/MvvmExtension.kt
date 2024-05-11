package com.example.Mvvm.base.extension

import android.os.Bundle
import com.example.Mvvm.BuildConfig

fun getAPIBaseUrl(): String {
    return if (BuildConfig.DEBUG) {
        "https://hot-box-dev-api.azurewebsites.net/"
    } else {
        "https://hot-box-dev-api.azurewebsites.net/"
    }
}

fun Bundle.putEnum(key:String, enum: Enum<*>){
    putString(key, enum.name)
}

inline fun <reified T: Enum<T>> Bundle.getEnum(key: String, default: T): T {
    val found = getString(key)
    return if (found == null) { default } else enumValueOf(found)
}