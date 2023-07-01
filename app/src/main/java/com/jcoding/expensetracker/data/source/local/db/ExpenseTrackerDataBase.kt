package com.jcoding.expensetracker.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jcoding.expensetracker.data.source.local.db.dao.ExpenseDao
import com.jcoding.expensetracker.data.source.local.db.entities.ExpenseEntity
import com.jcoding.expensetracker.data.source.local.db.typeconverters.RoomTypeConverter

@Database(entities = [ExpenseEntity::class], version = DbProperties.dbVersion , exportSchema = false)
@TypeConverters(RoomTypeConverter::class)
abstract class ExpenseTrackerDataBase : RoomDatabase(){

    abstract fun getExpenseDao() : ExpenseDao

    companion object {

        @Volatile private var instance : ExpenseTrackerDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) :ExpenseTrackerDataBase {
            return instance ?: synchronized(LOCK){
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ExpenseTrackerDataBase::class.java,
            DbProperties.databaseName
        ).build()

    }
}