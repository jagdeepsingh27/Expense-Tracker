package com.jcoding.expensetracker.util.resourceprovider

interface ResourceProvider {
    fun getString(resId: Int, vararg formatArgs: Any): String?
    fun getString(resId: Int): String?
}