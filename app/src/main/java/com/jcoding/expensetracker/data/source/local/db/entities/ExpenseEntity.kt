package com.jcoding.expensetracker.data.source.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpenseCategory
import com.jcoding.expensetracker.data.source.local.staticdataprovider.ExpensePaymentMethod
import java.util.Date


@Entity(tableName = "expense")
data class ExpenseEntity(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "amount")
    val amount: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "date")
    val date: Date,
    @ColumnInfo(name = "category")
    val category: ExpenseCategory,
    @ColumnInfo(name = "payment_method")
    val paymentMethod: ExpensePaymentMethod

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}