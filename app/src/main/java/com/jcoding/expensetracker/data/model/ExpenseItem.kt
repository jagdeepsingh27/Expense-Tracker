package com.jcoding.expensetracker.data.model

import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod
import java.util.Date

data class ExpenseItem(
    val title: String,
    val amount: String,
    val description: String,
    val date: Date,
    val category: ExpenseCategory,
    val paymentMethod: ExpensePaymentMethod
){
    var id:Int = 0
}


