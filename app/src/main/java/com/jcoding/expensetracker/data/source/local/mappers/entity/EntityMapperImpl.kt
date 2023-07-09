package com.jcoding.expensetracker.data.source.local.mappers.entity

import com.jcoding.expensetracker.data.model.ExpenseItem
import com.jcoding.expensetracker.data.source.local.db.entities.ExpenseEntity


class EntityMapperImpl : EntityMapper {
    override fun expenseItemToEntity(expenseItem: ExpenseItem): ExpenseEntity {
        return ExpenseEntity(
            title = expenseItem.title,
            amount = expenseItem.amount,
            description = expenseItem.description,
            date = expenseItem.date,
            category = expenseItem.category,
            paymentMethod = expenseItem.paymentMethod
        ).apply {
            id = expenseItem.id
        }
    }


    override fun expenseItemFromEntity(entity: ExpenseEntity): ExpenseItem {
        return ExpenseItem(
            title = entity.title,
            amount = entity.amount,
            description = entity.description,
            date = entity.date,
            category = entity.category,
            paymentMethod = entity.paymentMethod
        ).apply { id = entity.id }
    }
}