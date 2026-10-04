package kittoku.osc.preference.custom

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import kittoku.osc.R


internal class BrandLogoPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {
    init {
        isSelectable = false
        isEnabled = false
        layoutResource = R.layout.preference_brand_logo
    }
}