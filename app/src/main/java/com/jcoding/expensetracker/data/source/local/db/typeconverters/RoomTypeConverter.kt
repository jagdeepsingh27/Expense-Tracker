package com.jcoding.expensetracker.data.source.local.db.typeconverters

import androidx.room.TypeConverter
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod
import java.util.Date

class RoomTypeConverter {

    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }


    @TypeConverter
    fun toExpenseCategory(type: Int): ExpenseCategory {
        return ExpenseCategory.getItemByType(type)
    }

    @TypeConverter
    fun fromExpenseCategory(category: ExpenseCategory): Int {
        return category.type
    }

    @TypeConverter
    fun toExpensePaymentMethod(type: Int): ExpensePaymentMethod {
        return ExpensePaymentMethod.getItemByType(type)
    }

    @TypeConverter
    fun fromExpensePaymentMethod(paymentMethod: ExpensePaymentMethod): Int {
        return paymentMethod.type
    }

}