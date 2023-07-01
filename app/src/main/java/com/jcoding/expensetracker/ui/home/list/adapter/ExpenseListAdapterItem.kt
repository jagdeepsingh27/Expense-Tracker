package com.jcoding.expensetracker.ui.home.list.adapter

import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod
import java.util.Date

data class ExpenseListAdapterItem(
    val id: String,
    val title: String,
    var currencySymbol: String? = null,
    val amount: String,
    val description: String,
    val date: Date,
    val category: ExpenseCategory,
    val paymentMethod: ExpensePaymentMethod
) {


    companion object {
        fun fromExpenseItemModel(
            expenseItem: ExpenseItem,
            currencySymbol: String?
        ): ExpenseListAdapterItem {
            return ExpenseListAdapterItem(
                id = expenseItem.id.toString(),
                title = expenseItem.title,
                currencySymbol = currencySymbol,
                amount = expenseItem.amount,
                description = expenseItem.description,
                date = expenseItem.date,
                category = expenseItem.category,
                paymentMethod = expenseItem.paymentMethod
            )
        }
    }


}