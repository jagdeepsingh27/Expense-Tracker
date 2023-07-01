package com.jcoding.expensetracker.ui.home.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jcoding.expensetracker.Injection

class ExpenseListViewModelFactory() :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpenseListViewModel(
            resourceProvider = Injection.requireResourceProvider(),
            appRepository = Injection.requireAppRepository()
        ) as T
    }
}