package com.jcoding.expensetracker.ui.currency.list

data class CurrencyLisItemPayloadDiff(val isSelectionChanged: Boolean) {
    fun hasDifference() = isSelectionChanged
    operator fun plus(other: CurrencyLisItemPayloadDiff): CurrencyLisItemPayloadDiff =
        copy(
            isSelectionChanged = isSelectionChanged || other.isSelectionChanged
        )
}

