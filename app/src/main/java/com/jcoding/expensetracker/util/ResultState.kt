package com.jcoding.expensetracker.util

sealed class ResultState<out T : Any > {
    data class Success<out T : Any >(val data: T , val message: String? = null):ResultState<T>()
    data class Error(val message : String?) : ResultState<Nothing>()
}