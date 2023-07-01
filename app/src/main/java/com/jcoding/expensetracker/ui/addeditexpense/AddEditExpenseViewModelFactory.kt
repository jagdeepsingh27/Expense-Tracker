package com.jcoding.expensetracker.ui.addeditexpense

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jcoding.expensetracker.Injection


class AddEditExpenseViewModelFactory(val intent: Intent?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val actionMode = intent?.getSerializableExtra(
            AddEditActivityStartUpParamsKey.EXTRA_KEY_ACTION_MODE
        ) as? AddEditActivityActionMode ?: throw RuntimeException("action mode required")

        val expenseId = intent.getStringExtra(AddEditActivityStartUpParamsKey.EXTRA_KEY_EXPENSE_ID)
        if (actionMode == AddEditActivityActionMode.EDIT && expenseId.isNullOrBlank()) {
            throw RuntimeException("Expense Id required for Edit Mode")
        }

        return AddEditExpenseViewModel(
            actionMode = actionMode,
            expenseId = expenseId,
            resourceProvider = Injection.requireResourceProvider(),
            appRepository = Injection.requireAppRepository()
        ) as T
    }
}