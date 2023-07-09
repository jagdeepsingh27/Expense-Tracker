package com.jcoding.expensetracker.data.source

import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.model.SaveExpenseItemResponse
import com.jcoding.expensetracker.data.model.common.GeneralResponse
import com.jcoding.expensetracker.data.source.local.staticdataprovider.CurrencyItem
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod
import kotlinx.coroutines.flow.Flow
import java.util.ArrayList
import java.util.Date

interface AppRepository {
    suspend fun saveExpenseItem(expenseItem: ExpenseItem): GeneralResponse<SaveExpenseItemResponse>
    suspend fun fetchExpenseList(startDate: Date? = null, endDate: Date? = null)
            : GeneralResponse<List<ExpenseItem>>

    suspend fun fetchExpenseCategoryList(): GeneralResponse<ArrayList<ExpenseCategory>>
    suspend fun fetchPaymentMethodList(): GeneralResponse<ArrayList<ExpensePaymentMethod>>
    suspend fun fetchExpenseDetails(id: String): GeneralResponse<ExpenseItem>
    suspend fun deleteExpenseItem(expenseItem: ExpenseItem): GeneralResponse<Unit>
    suspend fun updateExpenseItem(expenseItem: ExpenseItem): GeneralResponse<Unit>
    suspend fun fetchCurrencyList(): GeneralResponse<ArrayList<CurrencyItem>>

    fun fetchSelectedCurrency(): Flow<CurrencyItem?>
    suspend fun saveSelectedCurrency(currencyItemId: String)
}