package kittoku.osc.preference

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast


internal fun toastInvalidSetting(message: String, context: Context) {
    Toast.makeText(context, "INVALID SETTING: $message", Toast.LENGTH_LONG).show()
}

internal fun checkPreferences(prefs: SharedPreferences): String? {
    return null
}