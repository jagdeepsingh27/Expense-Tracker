package com.jcoding.expensetracker.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jcoding.expensetracker.data.source.local.db.dao.ExpenseDao
import com.jcoding.expensetracker.data.source.local.db.entities.ExpenseEntity
import com.jcoding.expensetracker.data.source.local.db.typeconverters.RoomTypeConverter

@Database(entities = [ExpenseEntity::class], version = ExpenseTrackerDataBase.dbVersion , exportSchema = false)
@TypeConverters(RoomTypeConverter::class)
abstract class ExpenseTrackerDataBase : RoomDatabase(){
    abstract val expenseDao : ExpenseDao

    companion object  {
            const val dbVersion = 1
            const val DATABASE_NAME = "expense_database"
    }
}