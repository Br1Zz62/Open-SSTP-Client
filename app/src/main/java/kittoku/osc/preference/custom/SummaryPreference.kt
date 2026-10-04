package kittoku.osc.preference.custom

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import kittoku.osc.preference.LIST_TYPE_ALLOWED
import kittoku.osc.preference.OscPrefKey
import kittoku.osc.preference.accessor.getBooleanPrefValue
import kittoku.osc.preference.accessor.getSetPrefValue
import kittoku.osc.preference.accessor.getStringPrefValue


internal abstract class SummaryPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs), OscPreference {
    protected open val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == oscPrefKey.name) {
            updateView()
        }
    }

    override fun onAttached() {
        sharedPreferences!!.registerOnSharedPreferenceChangeListener(listener)

        initialize()
    }

    override fun onDetached() {
        sharedPreferences!!.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        holder.findViewById(android.R.id.summary)?.also {
            it as TextView
            it.maxLines = Int.MAX_VALUE
        }
    }
}

internal class HomeStatusPreference(context: Context, attrs: AttributeSet) : SummaryPreference(context, attrs) {
    override val oscPrefKey = OscPrefKey.HOME_STATUS
    override val parentKey: OscPrefKey? = null
    override val preferenceTitle = "Статус подключения"

    // Второй слушатель: HOME_STATUS меняется сервисом во время работы,
    // HOME_CONNECTOR — переключается при подключении/отключении VPN.
    // Реагируем на оба, чтобы summary всегда был актуальным.
    private val connectorListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == OscPrefKey.HOME_CONNECTOR.name) {
            updateView()
        }
    }

    init {
        onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val prefs = sharedPreferences!!
            val connected = getBooleanPrefValue(OscPrefKey.HOME_CONNECTOR, prefs)

            if (connected) {
                val details = getStringPrefValue(OscPrefKey.HOME_STATUS, prefs)
                    .ifEmpty { "Нет данных" }

                showCopyableDialog(
                    title = "Параметры подключения",
                    text = details
                )
            } else {
                showCopyableDialog(
                    title = "Статус подключения",
                    text = "VPN не подключён"
                )
            }

            true
        }
    }

    override fun onAttached() {
        super.onAttached()
        sharedPreferences!!.registerOnSharedPreferenceChangeListener(connectorListener)
    }

    override fun onDetached() {
        sharedPreferences!!.unregisterOnSharedPreferenceChangeListener(connectorListener)
        super.onDetached()
    }

    override fun updateView() {
        val connected = getBooleanPrefValue(OscPrefKey.HOME_CONNECTOR, sharedPreferences!!)
        summary = if (connected) "Подключено" else "Отключено"
    }

    // Диалог с выделяемым текстом и кнопкой "Копировать"
    private fun showCopyableDialog(title: String, text: String) {
        val padding = (16 * context.resources.displayMetrics.density).toInt()

        val textView = TextView(context).apply {
            this.text = text
            textSize = 14f
            setTextIsSelectable(true)
            setPadding(padding, padding / 2, padding, padding / 2)
        }

        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(textView)
            .setPositiveButton("Копировать") { _, _ ->
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Статус подключения", text))
                Toast.makeText(context, "Скопировано", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("OK", null)
            .show()
    }
}

internal class RouteSelectedAppsPreference(context: Context, attrs: AttributeSet) : SummaryPreference(context, attrs) {
    override val oscPrefKey = OscPrefKey.ROUTE_SELECTED_APPS
    override val parentKey = OscPrefKey.ROUTE_DO_ENABLE_APP_BASED_RULE
    override val preferenceTitle = "Select Allowed/Disallowed Apps"

    override fun updateView() {
        val isAllowedList = getStringPrefValue(OscPrefKey.ROUTE_APP_LIST_TYPE, sharedPreferences!!) == LIST_TYPE_ALLOWED
        val verb = if (isAllowedList) "Allowed" else "Disallowed"

        summary = when (val size = getSetPrefValue(oscPrefKey, sharedPreferences!!).size) {
            0 -> "[No App $verb]"
            1 -> "[1 App $verb]"
            else -> "[$size Apps $verb]"
        }
    }
}