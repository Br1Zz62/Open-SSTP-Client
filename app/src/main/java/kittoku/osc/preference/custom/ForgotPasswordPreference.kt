package kittoku.osc.preference.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceViewHolder
import kittoku.osc.R

internal class ForgotPasswordPreference(context: Context, attrs: AttributeSet) : LinkPreference(context, attrs) {
    override val preferenceTitle = "Забыли пароль?"
    override val preferenceSummary = ""
    override val url = "https://lk.rarus-cloud.ru/password-recovery"

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        holder.findViewById(android.R.id.title)?.also {
            it as TextView
            it.setTextColor(ContextCompat.getColor(context, R.color.blue_primary))
        }
    }
}