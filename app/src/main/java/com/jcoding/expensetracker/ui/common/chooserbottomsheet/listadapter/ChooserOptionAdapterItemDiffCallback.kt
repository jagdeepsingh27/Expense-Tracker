package com.jcoding.expensetracker.ui.common.chooserbottomsheet.listadapter

import androidx.recyclerview.widget.DiffUtil

internal object ChooserOptionAdapterItemDiffCallback
    : DiffUtil.ItemCallback<ChooserOption>() {
    override fun areItemsTheSame(
        oldItem: ChooserOption,
        newItem: ChooserOption
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ChooserOption,
        newItem: ChooserOption
    ): Boolean {
        return true
    }
}