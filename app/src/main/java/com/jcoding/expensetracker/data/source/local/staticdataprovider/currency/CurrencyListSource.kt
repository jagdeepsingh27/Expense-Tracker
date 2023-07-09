package com.jcoding.expensetracker.data.source.local.staticdataprovider.currency

import com.jcoding.expensetracker.data.source.local.staticdataprovider.CurrencyItem

interface CurrencyListSource {
    fun fetchCurrencyList(): ArrayList<CurrencyItem>
    fun fetchCurrencyItemById(id : Int?): CurrencyItem?
}