package com.jcoding.expensetracker.ui.home.list.adapter

data class ExpenseListPayloadDiff(
    val titleChanged: Boolean,
    val amountChanged: Boolean,
    val descriptionChanged: Boolean,
    val dateChanged: Boolean,
    val categoryChanged: Boolean,
    val paymentMethodChanged: Boolean,
    val currencySymbolChanged: Boolean
) {
    fun hasDifference(): Boolean {
        return titleChanged || amountChanged || descriptionChanged ||
                dateChanged || categoryChanged || paymentMethodChanged
                || currencySymbolChanged
    }

    operator fun plus(other: ExpenseListPayloadDiff): ExpenseListPayloadDiff =
        copy(
            titleChanged = titleChanged || other.titleChanged,
            amountChanged = amountChanged || other.amountChanged,
            descriptionChanged = descriptionChanged || other.descriptionChanged,
            dateChanged = dateChanged || other.dateChanged,
            categoryChanged = categoryChanged || other.categoryChanged,
            paymentMethodChanged = paymentMethodChanged || other.paymentMethodChanged,
            currencySymbolChanged = currencySymbolChanged || other.currencySymbolChanged
        )
}