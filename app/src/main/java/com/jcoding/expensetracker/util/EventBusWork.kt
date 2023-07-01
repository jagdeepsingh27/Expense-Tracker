package com.jcoding.expensetracker.util

import com.jcoding.expensetracker.data.model.ExpenseItem


object EventBusWork {
    data class UpdateExpenseItem(val item : ExpenseItem)
    data class AddsNewExpenseItem(val item : ExpenseItem)
    data class DeleteExpenseItem(val id:String)
}