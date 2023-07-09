package com.jcoding.expensetracker.data.source.local

import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.model.SaveExpenseItemResponse
import com.jcoding.expensetracker.data.model.common.GeneralResponse
import com.jcoding.expensetracker.data.source.local.db.dao.ExpenseDao
import com.jcoding.expensetracker.data.source.local.mappers.entity.EntityMapper
import com.jcoding.expensetracker.data.source.local.mappers.roomdb.RoomDbResponseMapper
import com.jcoding.expensetracker.data.source.local.preferences.AppPreferences
import com.jcoding.expensetracker.data.source.local.staticdataprovider.CurrencyItem
import com.jcoding.expensetracker.data.source.local.staticdataprovider.currency.CurrencyListSourceImpl
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod
import com.jcoding.expensetracker.data.source.local.staticdataprovider.currency.CurrencyListSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.ArrayList
import java.util.Date
import javax.inject.Inject


class LocalAppDataSourceImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val appPreferences: AppPreferences,
    private val entityMapper: EntityMapper,
    private val roomDbResponseMapper: RoomDbResponseMapper,
    private val currencyListSource: CurrencyListSource
) : LocalAppDataSource {
    override suspend fun saveExpenseItem(expenseItem: ExpenseItem): GeneralResponse<SaveExpenseItemResponse> {
        return withContext(Dispatchers.IO) {
            val entity = entityMapper.expenseItemToEntity(expenseItem)
            val itemId = expenseDao.addExpense(entity)
            roomDbResponseMapper.processResponse(SaveExpenseItemResponse(id = itemId))
        }
    }

    override suspend fun fetchExpenseList(
        startDate: Date?,
        endDate: Date?
    ): GeneralResponse<List<ExpenseItem>> {
        return withContext(Dispatchers.IO) {
            val resultList = expenseDao.fetchExpenseList(startDate, endDate).let { it ->
                val expenseItemList = ArrayList<ExpenseItem>()
                it.forEach { entity ->
                    expenseItemList.add(entityMapper.expenseItemFromEntity(entity))
                }
                expenseItemList
            }
            roomDbResponseMapper.processResponse(resultList)
        }
    }

    override suspend fun fetchExpenseCategoryList(): GeneralResponse<ArrayList<ExpenseCategory>> {
        return withContext(Dispatchers.IO) {
            roomDbResponseMapper.processResponse(ExpenseCategory.getList())
        }
    }

    override suspend fun fetchPaymentMethodList(): GeneralResponse<ArrayList<ExpensePaymentMethod>> {
        return withContext(Dispatchers.IO) {
            roomDbResponseMapper.processResponse(ExpensePaymentMethod.getList())
        }
    }

    override suspend fun fetchExpenseDetails(id: String): GeneralResponse<ExpenseItem> {
        return withContext(Dispatchers.IO) {
            roomDbResponseMapper.processResponse(
                entityMapper.expenseItemFromEntity(expenseDao.fetchExpenseItem(id))
            )
        }
    }

    override suspend fun deleteExpenseItem(expenseItem: ExpenseItem): GeneralResponse<Unit> {
        return withContext(Dispatchers.IO) {
            val entity = entityMapper.expenseItemToEntity(expenseItem)
            roomDbResponseMapper.processDeleteItemResponse(expenseDao.deleteExpenseItem(entity))
        }
    }

    override suspend fun updateExpenseItem(expenseItem: ExpenseItem): GeneralResponse<Unit> {
        return withContext(Dispatchers.IO) {
            val entity = entityMapper.expenseItemToEntity(expenseItem)
            roomDbResponseMapper.processUpdateItemResponse(expenseDao.updateExpense(entity))
        }
    }

    override suspend fun fetchCurrencyList(): GeneralResponse<java.util.ArrayList<CurrencyItem>> {
        return withContext(Dispatchers.IO) {
            roomDbResponseMapper.processResponse(currencyListSource.fetchCurrencyList())
        }
    }

    override fun fetchSelectedCurrency(): Flow<CurrencyItem?> {
        return appPreferences.fetchCurrencyId().map { id ->
            currencyListSource.fetchCurrencyItemById(id?.toInt())
        }
    }

    override suspend fun saveSelectedCurrency(currencyItemId: String) {
        appPreferences.saveCurrencyId(currencyItemId)
    }
}