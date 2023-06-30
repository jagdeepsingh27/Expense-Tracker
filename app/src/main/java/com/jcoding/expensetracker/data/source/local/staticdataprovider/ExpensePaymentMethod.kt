package com.jcoding.expensetracker.data.source.local.staticdataprovider

import com.jcoding.expensetracker.R

/***
 * This enum class contains the PaymentMethod for the expense
 * Each Payment Method have the type , name resource string id and drawable resource id
 * Here "0" Type value denotes the others
 */
//todo replace with database in next version
enum class ExpensePaymentMethod(val type: Int, val nameResId: Int) {
    Cash(1, R.string.expensePaymentCash),
    Digital(2, R.string.expensePaymentDigital),
    Cheque(3, R.string.expensePaymentCheque),
    Others(0, R.string.expensePaymentOthers);

    companion object {
        /** this function return the Payment Method of input type */
        fun getItemByType(type: Int): ExpensePaymentMethod {
            return when (type) {
                0 -> Others
                1 -> Cash
                2 -> Digital
                3 -> Cheque
                else -> throw RuntimeException("invalid payment method type")
            }
        }


        fun getList(): ArrayList<ExpensePaymentMethod> {
            return ArrayList<ExpensePaymentMethod>().apply {
                add(Cash)
                add(Digital)
                add(Cheque)
                add(Others)
            }
        }
    }
}