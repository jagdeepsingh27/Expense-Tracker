package com.jcoding.expensetracker

import android.content.Context
import com.jcoding.expensetracker.data.source.AppRepository
import com.jcoding.expensetracker.data.source.local.LocalAppDataSourceImpl
import com.jcoding.expensetracker.data.source.local.db.ExpenseTrackerDataBase
import com.jcoding.expensetracker.data.source.local.mappers.EntityMapper
import com.jcoding.expensetracker.data.source.local.mappers.RoomDbResponseMapper
import com.jcoding.expensetracker.data.source.local.preferences.AppPreferences
import com.jcoding.expensetracker.data.source.local.staticdataprovider.CurrencyListSource
import com.jcoding.expensetracker.util.resourceprovider.ResourceProvider
import com.jcoding.expensetracker.util.resourceprovider.ResourceProviderImpl

/** This class used for the manually dependency injection */
object Injection {
    //this is called from the Application class for initialized required content
    fun init(context: Context) {
        setAppRepository(context)
        setResourceProvider(context)
    }

    //////////////////////////////////////////////////////////////////////////////
    /* repository initialization */
    private var appRepository: AppRepository? = null
    private fun setAppRepository(context: Context) {
        val localAppDataSource = LocalAppDataSourceImpl(
            ExpenseTrackerDataBase(context).getExpenseDao(),
            AppPreferences(context.dataStore),
            EntityMapper(),
            RoomDbResponseMapper(),
            CurrencyListSource()
        )
        appRepository = AppRepository(localAppDataSource = localAppDataSource)
    }

    fun requireAppRepository() = requireNotNull(appRepository)

    //////////////////////////////////////////////////////////////////////////////
    /* resource provider initialization */
    private var resourceProvider: ResourceProvider? = null
    private fun setResourceProvider(context: Context) {
        resourceProvider = ResourceProviderImpl(context)
    }

    fun requireResourceProvider() = requireNotNull(resourceProvider)
    //////////////////////////////////////////////////////////////////////////////
}