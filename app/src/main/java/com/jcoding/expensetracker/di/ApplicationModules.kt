package com.jcoding.expensetracker.di

import android.content.Context
import com.jcoding.expensetracker.data.source.AppRepository
import com.jcoding.expensetracker.data.source.local.LocalAppDataSource
import com.jcoding.expensetracker.util.resourceprovider.ResourceProvider
import com.jcoding.expensetracker.util.resourceprovider.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object  ApplicationModules {


    @Provides
    @Singleton
    fun provideResourceProvider(@ApplicationContext appContext: Context): ResourceProvider {
        return ResourceProviderImpl(appContext)
    }



}