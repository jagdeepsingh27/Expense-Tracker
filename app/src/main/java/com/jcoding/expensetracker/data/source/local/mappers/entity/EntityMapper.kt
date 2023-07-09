package com.jcoding.expensetracker.data.source.local.mappers.entity

import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.source.local.db.entities.ExpenseEntity

interface EntityMapper {
    fun expenseItemToEntity(expenseItem: ExpenseItem): ExpenseEntity
    fun expenseItemFromEntity(entity: ExpenseEntity): ExpenseItem
}