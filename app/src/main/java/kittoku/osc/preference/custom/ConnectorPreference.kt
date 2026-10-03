package kittoku.osc.preference.custom

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import kittoku.osc.R
import kittoku.osc.preference.OscPrefKey
import kittoku.osc.preference.accessor.getBooleanPrefValue


internal class HomeConnectorPreference(context: Context, attrs: AttributeSet) : SwitchPreferenceCompat(context, attrs), OscPreference {
    override val oscPrefKey = OscPrefKey.HOME_CONNECTOR
    override val parentKey: OscPrefKey? = null
    override val preferenceTitle = ""

    init {
        layoutResource = R.layout.preference_connector
        widgetLayoutResource = 0
    }

    override fun updateView() {
        isChecked = getBooleanPrefValue(oscPrefKey, sharedPreferences!!)
        notifyChanged()
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val button = holder.findViewById(R.id.connectorButton) as TextView
        if (isChecked) {
            button.text = context.getString(R.string.disconnect)
            button.setTextColor(ContextCompat.getColor(context, R.color.text_light))
            button.setBackgroundResource(R.drawable.bg_button_disconnect)
        } else {
            button.text = context.getString(R.string.connect)
            button.setTextColor(ContextCompat.getColor(context, R.color.white))
            button.setBackgroundResource(R.drawable.bg_button_connect)
        }
    }

    private var listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == oscPrefKey.name) {
            updateView()
        }
    }

    override fun onAttached() {
        sharedPreferences!!.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onDetached() {
        sharedPreferences!!.unregisterOnSharedPreferenceChangeListener(listener)
    }
}