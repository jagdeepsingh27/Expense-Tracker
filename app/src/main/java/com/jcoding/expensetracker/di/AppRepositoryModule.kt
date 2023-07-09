package com.jcoding.expensetracker.di

import com.jcoding.expensetracker.data.source.AppRepository
import com.jcoding.expensetracker.data.source.AppRepositoryImpl
import com.jcoding.expensetracker.data.source.local.LocalAppDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppRepositoryModule {
    @Provides
    @Singleton
    fun provideAppRepository(localDataSource:LocalAppDataSource): AppRepository {
        return AppRepositoryImpl(localDataSource)
    }
}

