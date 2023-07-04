package com.jcoding.expensetracker.ui.currency.list


internal fun CurrencyListAdapterItem.diff(other: CurrencyListAdapterItem): CurrencyLisItemPayloadDiff {
    return CurrencyLisItemPayloadDiff(
        isSelectionChanged = isSelected != other.isSelected,
    )
}

