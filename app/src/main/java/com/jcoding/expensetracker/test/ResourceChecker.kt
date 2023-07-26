package com.jcoding.expensetracker.test

import android.content.Context

class ResourceChecker {
    fun isEqual(context:Context,resId:Int,string:String):Boolean{
        return context.getString(resId) == string
    }
}