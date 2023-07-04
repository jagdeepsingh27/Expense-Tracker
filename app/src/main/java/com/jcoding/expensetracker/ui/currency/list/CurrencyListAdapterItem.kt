package com.jcoding.expensetracker.ui.currency.list

import com.jcoding.expensetracker.data.source.local.staticdataprovider.CurrencyItem

data class CurrencyListAdapterItem(
    val id: Int,
    val symbol: String,
    var isSelected: Boolean = false
) {
    companion object {
        fun fromCurrencyItem(currencyItem: CurrencyItem): CurrencyListAdapterItem {
            return CurrencyListAdapterItem(
                id = currencyItem.id,
                symbol = currencyItem.symbol
            )
        }
    }
}

