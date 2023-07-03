package com.jcoding.expensetracker.ui.expensedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jcoding.expensetracker.Injection


class ExpenseDetailsViewModelFactory(private val expenseId:String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(expenseId.isBlank()){
            throw RuntimeException("expense id is blank")
        }

        return ExpenseDetailsViewModel(
            expenseId = expenseId,
            resourceProvider = Injection.requireResourceProvider(),
            appRepository = Injection.requireAppRepository()
        ) as T
    }
}