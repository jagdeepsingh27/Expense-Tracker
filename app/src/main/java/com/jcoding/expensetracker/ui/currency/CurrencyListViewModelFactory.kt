package com.jcoding.expensetracker.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jcoding.expensetracker.Injection


class CurrencyListViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurrencyListViewModel(
            Injection.requireResourceProvider(),
            Injection.requireAppRepository()
        ) as T
    }
}