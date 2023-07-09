package com.jcoding.expensetracker.di

import android.content.Context
import com.jcoding.expensetracker.data.source.local.LocalAppDataSource
import com.jcoding.expensetracker.data.source.local.LocalAppDataSourceImpl
import com.jcoding.expensetracker.data.source.local.db.dao.ExpenseDao
import com.jcoding.expensetracker.data.source.local.mappers.entity.EntityMapper
import com.jcoding.expensetracker.data.source.local.mappers.entity.EntityMapperImpl
import com.jcoding.expensetracker.data.source.local.mappers.roomdb.RoomDbResponseMapper
import com.jcoding.expensetracker.data.source.local.mappers.roomdb.RoomDbResponseMapperImpl
import com.jcoding.expensetracker.data.source.local.preferences.AppPreferences
import com.jcoding.expensetracker.data.source.local.staticdataprovider.currency.Currency
import com.jcoding.expensetracker.data.source.local.staticdataprovider.currency.CurrencyListSource
import com.jcoding.expensetracker.data.source.local.staticdataprovider.currency.CurrencyListSourceImpl
import com.jcoding.expensetracker.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object  LocalDataSourceModule {
    @Provides
    @Singleton
    fun provideLocalAppDataSource(
        expenseDao: ExpenseDao,
        appPreferences: AppPreferences,
        entityMapper: EntityMapper,
        roomDbResponseMapper: RoomDbResponseMapper,
        currencyListSource: CurrencyListSource
    ): LocalAppDataSource {
        return LocalAppDataSourceImpl(
            expenseDao,
            appPreferences,
            entityMapper,
            roomDbResponseMapper,
            currencyListSource
        )
    }

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext appContext: Context): AppPreferences {
        return AppPreferences(appContext.dataStore)
    }


    @Provides
    @Singleton
    fun provideEntityMapper(): EntityMapper = EntityMapperImpl()

    @Provides
    @Singleton
    fun provideRoomDbResponseMapper(): RoomDbResponseMapper = RoomDbResponseMapperImpl()


    @Provides
    @Singleton
    fun provideCurrencyListSource(): CurrencyListSource = CurrencyListSourceImpl()
}