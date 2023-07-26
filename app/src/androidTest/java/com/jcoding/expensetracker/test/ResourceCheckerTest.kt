package com.jcoding.expensetracker.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.junit.Before


class ResourceCheckerTest{
    private lateinit var resourceChecker :ResourceChecker

    @Before
    fun setup(){
         resourceChecker =  ResourceChecker()
    }

    @Before
    fun destroy(){

    }


    @Test
    fun stringResourceEqualsToGiveStringReturnsTrue(){
        resourceChecker =  ResourceChecker()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceChecker.isEqual(context,
            com.jcoding.expensetracker.R.string.app_name, "Expense-Tracker")
        assertThat(result).isTrue()
    }

    @Test
    fun stringResourceNotEqualsToGiveStringReturnsFalse(){
        resourceChecker =  ResourceChecker()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = resourceChecker.isEqual(context,
            com.jcoding.expensetracker.R.string.app_name, "Expense")
        assertThat(result).isFalse()
    }
}