package com.jcoding.expensetracker.ui.addeditexpense

import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod

/**
 * this class contains the required resource for the AddEdit functionality
 * @param existingExpenseItem is null for add mode and will only hold value for edit mode
 */
data class AddEditExpenseRequiredResourceResult(
    val expenseCategoryList: ArrayList<ExpenseCategory>,
    val expensePaymentMethodList: ArrayList<ExpensePaymentMethod>,
    val existingExpenseItem : ExpenseItem? = null
)
