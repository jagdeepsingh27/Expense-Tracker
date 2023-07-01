package com.jcoding.expensetracker.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel() : ViewModel() {
    protected fun getResolvedCurrencyValue(currencySymbol:String?, amount:String ):String{
        val symbol = currencySymbol ?: ""
        return  "$symbol $amount"
    }
}