package com.jcoding.expensetracker.data.source.local.staticdataprovider.currency

import com.jcoding.expensetracker.data.source.local.staticdataprovider.CurrencyItem
import javax.inject.Inject

class CurrencyListSourceImpl @Inject constructor() : CurrencyListSource {
    private fun currencyEnumToCurrencyItem(item: Currency): CurrencyItem {
        return CurrencyItem(
            id = item.id,
            symbol = item.symbol
        )
    }

    override fun fetchCurrencyList(): ArrayList<CurrencyItem> {
        val currencyItemList = ArrayList<CurrencyItem>()
        currencyItemList.apply {
            add(currencyEnumToCurrencyItem(Currency.INDIAN_RUPEE))
            add(currencyEnumToCurrencyItem(Currency.DOLLAR))
            add(currencyEnumToCurrencyItem(Currency.EURO))
            add(currencyEnumToCurrencyItem(Currency.POUND))
            add(currencyEnumToCurrencyItem(Currency.OTHERS))
        }
        return currencyItemList
    }


    override fun fetchCurrencyItemById(id : Int?): CurrencyItem?{
        if(id == null){
            return null
        }
        return currencyEnumToCurrencyItem(Currency.getItemById(id))
    }
}