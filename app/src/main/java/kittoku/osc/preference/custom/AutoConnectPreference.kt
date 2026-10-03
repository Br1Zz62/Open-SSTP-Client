package kittoku.osc.preference.custom

import android.content.Context
import android.util.AttributeSet
import kittoku.osc.preference.OscPrefKey

internal class AutoConnectPreference(context: Context, attrs: AttributeSet) : ModifiedCheckBoxPreference(context, attrs) {
    override val oscPrefKey = OscPrefKey.HOME_AUTO_CONNECT
    override val parentKey: OscPrefKey? = null
    override val preferenceTitle = "Автоматический вход"
}