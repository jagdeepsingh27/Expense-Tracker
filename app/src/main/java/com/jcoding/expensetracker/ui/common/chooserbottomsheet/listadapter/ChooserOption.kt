package com.jcoding.expensetracker.ui.common.chooserbottomsheet.listadapter

import android.content.Context


data class ChooserOption(
    val id: String,
    val title: String? = null,
    val titleResId: Int? = null,
    val iconResId: Int? = null
) {
    fun getTitle(context: Context): String? {
        return if (titleResId != null) {
            context.getString(titleResId)
        } else {
            title
        }
    }
}