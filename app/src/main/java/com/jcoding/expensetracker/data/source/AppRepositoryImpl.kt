package com.jcoding.expensetracker.data.source

import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.model.SaveExpenseItemResponse
import com.jcoding.expensetracker.data.model.common.GeneralResponse
import com.jcoding.expensetracker.data.source.local.LocalAppDataSource
import com.jcoding.expensetracker.data.source.local.staticdataprovider.CurrencyItem
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod
import kotlinx.coroutines.flow.Flow
import java.util.ArrayList
import java.util.Date
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val localAppDataSource: LocalAppDataSource
) : AppRepository {
    override suspend fun saveExpenseItem(expenseItem: ExpenseItem): GeneralResponse<SaveExpenseItemResponse> {
        return localAppDataSource.saveExpenseItem(expenseItem)
    }

    override suspend fun fetchExpenseList(
        startDate: Date?,
        endDate: Date?
    ): GeneralResponse<List<ExpenseItem>> {
        return localAppDataSource.fetchExpenseList(startDate, endDate)
    }

    override suspend fun fetchExpenseCategoryList(): GeneralResponse<ArrayList<ExpenseCategory>> {
        return localAppDataSource.fetchExpenseCategoryList()
    }

    override suspend fun fetchPaymentMethodList(): GeneralResponse<ArrayList<ExpensePaymentMethod>> {
        return localAppDataSource.fetchPaymentMethodList()
    }

    override suspend fun fetchExpenseDetails(id: String): GeneralResponse<ExpenseItem> {
        return localAppDataSource.fetchExpenseDetails(id)
    }

    override suspend fun deleteExpenseItem(expenseItem: ExpenseItem): GeneralResponse<Unit> {
        return localAppDataSource.deleteExpenseItem(expenseItem)
    }

    override suspend fun updateExpenseItem(expenseItem: ExpenseItem): GeneralResponse<Unit> {
        return localAppDataSource.updateExpenseItem(expenseItem)
    }

    override suspend fun fetchCurrencyList(): GeneralResponse<java.util.ArrayList<CurrencyItem>> {
        return localAppDataSource.fetchCurrencyList()
    }

    override fun fetchSelectedCurrency(): Flow<CurrencyItem?> {
        return localAppDataSource.fetchSelectedCurrency()
    }

    override suspend fun saveSelectedCurrency(currencyItemId: String) {
        localAppDataSource.saveSelectedCurrency(currencyItemId)
    }
}