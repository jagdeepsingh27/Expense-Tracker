package com.jcoding.expensetracker.ui.home.list.adapter

internal fun ExpenseListAdapterItem.diff(other: ExpenseListAdapterItem): ExpenseListPayloadDiff {
    return ExpenseListPayloadDiff(
        titleChanged = title != other.title,
        amountChanged = amount != other.amount,
        descriptionChanged = description != other.description,
        dateChanged = date.time != other.date.time,
        categoryChanged = category.type != other.category.type,
        paymentMethodChanged = paymentMethod.type != other.paymentMethod.type,
        currencySymbolChanged = currencySymbol != other.currencySymbol
    )
}
