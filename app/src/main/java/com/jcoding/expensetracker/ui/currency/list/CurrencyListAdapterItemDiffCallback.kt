package com.jcoding.expensetracker.ui.currency.list

import androidx.recyclerview.widget.DiffUtil


internal object CurrencyListAdapterItemDiffCallback
    : DiffUtil.ItemCallback<CurrencyListAdapterItem>() {
    override fun areItemsTheSame(
        oldItem: CurrencyListAdapterItem,
        newItem: CurrencyListAdapterItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CurrencyListAdapterItem,
        newItem: CurrencyListAdapterItem
    ): Boolean {
        return oldItem.diff(newItem).hasDifference().not()
    }

    override fun getChangePayload(
        oldItem: CurrencyListAdapterItem,
        newItem: CurrencyListAdapterItem
    ): Any {
        // only called if their contents aren't the same,
        return oldItem.diff(newItem)
    }
}
