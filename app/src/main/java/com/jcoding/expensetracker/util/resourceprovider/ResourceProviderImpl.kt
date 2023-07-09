package com.jcoding.expensetracker.util.resourceprovider

import android.content.Context
import javax.inject.Inject


class ResourceProviderImpl @Inject constructor(val context: Context?) : ResourceProvider {
    override fun getString(resId: Int, vararg formatArgs: Any): String? {
        return context?.getString(resId, *formatArgs)
    }

    override fun getString(resId: Int): String? {
        return context?.getString(resId)
    }
}