package com.jcoding.expensetracker.ui.home.list.adapter

import androidx.recyclerview.widget.DiffUtil

internal object ExpenseListAdapterItemDiffCallback :
    DiffUtil.ItemCallback<ExpenseListAdapterItem>() {
    override fun areItemsTheSame(
        oldItem: ExpenseListAdapterItem,
        newItem: ExpenseListAdapterItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ExpenseListAdapterItem,
        newItem: ExpenseListAdapterItem
    ): Boolean {
        return oldItem.diff(newItem).hasDifference().not()
    }

    override fun getChangePayload(
        oldItem: ExpenseListAdapterItem,
        newItem: ExpenseListAdapterItem
    ): Any {
        // only called if their contents aren't the same,
        return oldItem.diff(newItem)
    }
}