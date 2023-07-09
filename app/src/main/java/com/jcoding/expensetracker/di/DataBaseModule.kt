package com.jcoding.expensetracker.di

import android.content.Context
import androidx.room.Room
import com.jcoding.expensetracker.data.source.local.LocalAppDataSource
import com.jcoding.expensetracker.data.source.local.db.ExpenseTrackerDataBase
import com.jcoding.expensetracker.data.source.local.db.dao.ExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ExpenseTrackerDataBase {
        return ExpenseTrackerDataBase(appContext)
    }

    @Provides
    @Singleton
    fun provideExpenseDao(expenseTrackerDataBase: ExpenseTrackerDataBase): ExpenseDao {
        return expenseTrackerDataBase.getExpenseDao()
    }

}