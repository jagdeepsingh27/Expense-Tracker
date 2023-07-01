package com.jcoding.expensetracker

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "expense_app_prefs")

class ExpenseTrackerApp() : Application() {
    override fun onCreate() {
        super.onCreate()
        //////////////////////////////////////////////////////////////////////
        //init injection
        initInjection()
        //////////////////////////////////////////////////////////////////////
    }

    private fun initInjection() {
        Injection.init(applicationContext)
    }
}