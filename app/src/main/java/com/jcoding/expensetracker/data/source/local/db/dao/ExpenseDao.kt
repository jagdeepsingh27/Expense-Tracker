package com.jcoding.expensetracker.data.source.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jcoding.expensetracker.data.source.local.db.entities.ExpenseEntity
import java.util.Date


@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense order by date desc")
    suspend fun fetchAllExpenseList(): List<ExpenseEntity>

    @Query("SELECT * FROM expense  WHERE date >= :lowerBound  order by date desc")
    suspend fun fetchExpenseListAboveLowerBound(lowerBound: Date): List<ExpenseEntity>

    @Query("SELECT * FROM expense WHERE date < :upperBound order by date desc")
    suspend fun fetchExpenseListBelowUpperBound(upperBound : Date): List<ExpenseEntity>

    @Query("SELECT * FROM expense WHERE date >= :lowerBound AND date < :upperBound order by date desc")
    suspend fun fetchExpenseListWithLowerAndUpperBound(lowerBound: Date, upperBound : Date): List<ExpenseEntity>


    suspend fun fetchExpenseList(lowerBound: Date?, upperBound : Date?): List<ExpenseEntity>{
        if(lowerBound == null && upperBound == null){
            return fetchAllExpenseList()
        }
        if(lowerBound != null && upperBound == null){
            return fetchExpenseListAboveLowerBound(lowerBound)
        }

        if(lowerBound == null && upperBound != null){
            return fetchExpenseListBelowUpperBound(upperBound)
        }
        if(lowerBound != null && upperBound != null){
            return fetchExpenseListWithLowerAndUpperBound(lowerBound,upperBound)
        }
        return fetchAllExpenseList()
    }



    @Query("SELECT * FROM expense WHERE id =:id")
    suspend fun fetchExpenseItem(id: String): ExpenseEntity

    @Insert
    suspend fun addExpense(entity: ExpenseEntity) : Long

    /* A @Delete method can optionally return an int value
    indicating the number of rows that were deleted successfully. */
    @Delete
    suspend fun deleteExpenseItem(entity: ExpenseEntity) : Int

    /* An @Update method can optionally
    return an int value indicating the number of rows that were updated successfully. */
    @Update
    suspend fun updateExpense(entity:  ExpenseEntity) : Int




    //    @Insert
//    fun insertMultiple(vararg gdf: Expense)


}