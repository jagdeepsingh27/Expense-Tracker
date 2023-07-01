package com.jcoding.expensetracker.data.source.local.mappers

import com.jcoding.expensetracker.data.model.common.GeneralResponse

class RoomDbResponseMapper {
    companion object {
        const val ERROR_SOMETHING_WENT_WRONG = "Something went wrong"
        private const val SUCCESS_MESSAGE = "Result success"
    }


    /*
    The @Query annotation allows you to write SQL statements and expose them as DAO methods.
    Use these query methods to query data from your app's database,
    or when you need to perform more complex inserts, updates, and deletes.
    Room validates SQL queries at compile time.
    This means that if there's a problem with your query,
     a compilation error occurs instead of a runtime failure.
     */

    /*
    If the @Insert method receives a single parameter,
    it can return a long value, which is the new rowId for the inserted item.
    If the parameter is an array or a collection,
     then the method should return an array or a collection of long values instead,
    with each value as the rowId for one of the inserted items. */


    fun <T> processResponse(input: T): GeneralResponse<T> {
        val generalResult = GeneralResponse<T>(true, SUCCESS_MESSAGE)
        generalResult.data = input
        return generalResult
    }

    /* A @Delete method can optionally return an int value indicating
           the number of rows that were deleted successfully. */
    fun  processDeleteItemResponse(input: Int): GeneralResponse<Unit> {
        val isSuccessStatus = input > 0
        val message = if(isSuccessStatus){
            SUCCESS_MESSAGE
        }else{
            ERROR_SOMETHING_WENT_WRONG
        }
        val generalResult = GeneralResponse<Unit>(isSuccessStatus, message)
        generalResult.data = Unit
        return generalResult
    }


    /* An @Update method can optionally
     return an int value indicating the number of rows that were updated successfully. */
    fun  processUpdateItemResponse(input: Int): GeneralResponse<Unit> {
        val isSuccessStatus = input > 0
        val message = if(isSuccessStatus){
            SUCCESS_MESSAGE
        }else{
            ERROR_SOMETHING_WENT_WRONG
        }
        val generalResult = GeneralResponse<Unit>(isSuccessStatus, message)
        generalResult.data = Unit
        return generalResult
    }



}