package com.leon.su.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.blankj.utilcode.util.LogUtils
import com.leon.su.domain.UserResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline var SharedPreferences.users: UserResponse?
    get() = getString("user_data", null)?.let {
        Json.decodeFromString(it)
    }
    set(value) = set("user_data", Json.encodeToString(value))

fun SharedPreferences.set(key: String, value: Any?) {
    when (value) {
        is String? -> edit { putString(key, value) }
        is Int -> edit { putInt(key, value.toInt()) }
        is Boolean -> edit { putBoolean(key, value) }
        is Float -> edit { putFloat(key, value.toFloat()) }
        is Long -> edit { putLong(key, value.toLong()) }
        else -> {
            LogUtils.e("Unsupported Type: $value")
        }
    }
}
